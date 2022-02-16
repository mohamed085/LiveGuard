package com.liveguard.service;

import com.liveguard.domain.EmailSendStatus;
import com.liveguard.domain.EmailSettingBag;
import com.liveguard.domain.VerificationCode;
import com.liveguard.util.PrepareMailSenderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Set;

@Slf4j
@Service
public class EmailService {

    private final SettingService settingService;
    private final VerificationCodeService verificationCodeService;

    public EmailService(SettingService settingService, VerificationCodeService verificationCodeService) {
        this.settingService = settingService;
        this.verificationCodeService = verificationCodeService;
    }

    public void sendVerificationEmail(VerificationCode code) throws UnsupportedEncodingException, MessagingException {
        EmailSettingBag emailSetting = settingService.getEmailSettings();

        log.debug("EmailService | Send verification email | emailSetting: " + emailSetting.getSenderName());
        JavaMailSenderImpl mailSender = PrepareMailSenderUtil.prepareMailSender(emailSetting);

        String subject = emailSetting.getCustomerVerifySubject();
        String content = emailSetting.getCustomerVerifyContent();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);

        messageHelper.setFrom(emailSetting.getFromAddress(), emailSetting.getSenderName());
        messageHelper.setTo(code.getUser().getEmail());
        messageHelper.setSubject(subject);

        content = content.replace("[[name]]", code.getUser().getName());
        content = content.replace("[[Verify Code]]", code.getCode());

        messageHelper.setText(content);

        mailSender.send(message);

        log.debug("EmailService | Send verification email | email send");

        verificationCodeService.updateEmailSendStatus(code.getUser().getId(), EmailSendStatus.SEND);
    }

    @Scheduled(fixedRate = 7200000L)
    public void sendVerificationEmailToUnsendedJob() throws UnsupportedEncodingException, MessagingException {
        log.debug("EmailService | sendVerificationEmailToUnsendedJob created");
        Set<VerificationCode> codes = verificationCodeService.findUnsendedEmail();
        if (codes.isEmpty()) return;

        for (VerificationCode code: codes) {
            log.debug("EmailService | Send verification email to unsended job | send verify code to: " + code.getUser().getEmail());
            sendVerificationEmail(code);
            log.debug("EmailService | Send verification email to unsended job  | Send verification email | email send");

        }
    }
}
