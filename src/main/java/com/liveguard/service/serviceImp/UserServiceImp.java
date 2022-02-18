package com.liveguard.service.serviceImp;

import com.liveguard.domain.AuthenticationType;
import com.liveguard.domain.User;
import com.liveguard.domain.VerificationCode;
import com.liveguard.dto.UserDTO;
import com.liveguard.exciptions.BadRequestException;
import com.liveguard.exciptions.EmailAlreadyExistsException;
import com.liveguard.exciptions.NotFoundException;
import com.liveguard.mapper.UserMapper;
import com.liveguard.payload.ApiResponse;
import com.liveguard.payload.ResendVerifyMailRequest;
import com.liveguard.payload.VerifyAccountRequest;
import com.liveguard.payload.VerifyAccountResponse;
import com.liveguard.repository.UserRepository;
import com.liveguard.repository.UserRoleRepository;
import com.liveguard.service.EmailService;
import com.liveguard.service.TokenService;
import com.liveguard.service.UserService;
import com.liveguard.service.VerificationCodeService;
import com.liveguard.util.DateConverterUtil;
import com.liveguard.util.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;
    private final VerificationCodeService verificationCodeService;
    private final EmailService emailService;
    private final TokenService tokenService;

    public UserServiceImp(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserRoleRepository userRoleRepository, VerificationCodeService verificationCodeService, EmailService emailService, TokenService tokenService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userRoleRepository = userRoleRepository;
        this.verificationCodeService = verificationCodeService;
        this.emailService = emailService;
        this.tokenService = tokenService;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Boolean userExistByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    @Override
    public String login(String username, String password) {
        log.debug("UserService | Login | user try to login: " + username);

        User user = findByEmail(username)
                .orElseThrow(() -> {
                    log.warn("UserService | Login | user not found: " + username);
                    throw new NotFoundException("This email not exist");
                });

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        log.debug("UserService | Login | user try to login: " + authenticate.getDetails());

        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = tokenService.generateToken(authenticate);

        return token;
    }

    @Override
    public User save(User user) {
        if(userRepository.existsByEmail(user.getEmail())) {
            log.warn("UserService | Register User | User: " + user.getEmail() + " email already exists");
            throw new EmailAlreadyExistsException(String.format("Email %s already exists", user.getEmail()));
        }
        userRepository.save(user);

        VerificationCode code = verificationCodeService.generateCode(user);
        verificationCodeService.save(code);

        return user;
    }

    @Override
    public User register(UserDTO userDTO) {
        User user = UserMapper.UserDTOToUser(userDTO);

        log.debug("UserService | Register User | User: " + user.getEmail() + " try to register");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(userRoleRepository.findAll().get(0));
        user.setCreatedTime(new Date());
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setEnable(false);
        user.setAuthenticationType(AuthenticationType.DATABASE);

        try {
            save(user);
            emailService.sendVerificationEmail(verificationCodeService.findByUserId(user.getId()));
        } catch (EmailAlreadyExistsException e) {
            throw new BadRequestException(e.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new BadRequestException(e.getMessage());
        }

        return user;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public VerifyAccountResponse verifyAccount(VerifyAccountRequest request) {
        log.debug("UserService | VerifyAccount | verify: " + request.getUserEmail());

        User user = findByEmail(request.getUserEmail())
                .orElseThrow(() -> new NotFoundException("This email not found"));

        VerificationCode code = verificationCodeService.findByUserId(user.getId());

        if (code.getTempVerify() < 5) {
            return CheckVerifyCode(request, code);
        }
        else {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime createdDate = DateConverterUtil.convertToLocalDateTime(code.getCreateDate());

            log.debug("UserService | VerifyAccount | date now: " + now);
            log.debug("UserService | VerifyAccount | created date now: " + createdDate);

            Duration duration = Duration.between(now, createdDate);
            long diff = Math.abs(duration.toMinutes());

            log.debug("UserService | VerifyAccount | diff date: " + diff);

            if (diff > 60) {
                code.getUser().setAccountNonLocked(true);
                code.setTempVerify(0);
                return CheckVerifyCode(request, code);
            }
            else {
                return new VerifyAccountResponse("You are blocked now", code.getUser().getEmail());
            }
        }
    }

    @Override
    public User resendVerifyAccount(ResendVerifyMailRequest request) {
        log.debug("UserService | resendVerifyAccount | user: " + request.getUserEmail());

        User user = findByEmail(request.getUserEmail())
                .orElseThrow(() ->  new NotFoundException("This email not exist"));

        try {
            emailService.sendVerificationEmail(verificationCodeService.findByUserId(user.getId()));
        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new BadRequestException(e.getMessage());
        }

        return user;
    }

    @Override
    public UserDTO userAccount() {
        log.debug("UserService | userAccount | get user authenticated account");
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        log.debug("UserService | userAccount | user email: " + userEmail);

        User user = findByEmail(userEmail)
                .orElseThrow(() ->  new NotFoundException("This email not exist"));

        UserDTO userDTO = UserMapper.UserToUserDTO(user);
        log.debug("UserService | userAccount | user info: " + userDTO.toString());

        return userDTO;
    }

    @Override
    public UserDTO updateCurrentUser(UserDTO userDTO) {

        log.debug("UserService | updateCurrentUser | get user authenticated account");
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        log.debug("UserService | updateCurrentUser | user email: " + userEmail);
        User user = findByEmail(userEmail)
                .orElseThrow(() ->  new NotFoundException("This email not exist"));

        if (userDTO.getName() != null)
            user.setName(userDTO.getName());

        if (userDTO.getPhone() != null)
            user.setPhone(userDTO.getPhone());

        if (userDTO.getAddress() != null)
            user.setAddress(userDTO.getAddress());

        if (userDTO.getDob() != null)
            user.setDob(userDTO.getDob());

        User savedUser = userRepository.save(user);
        return UserMapper.UserToUserDTO(savedUser);
    }

    @Override
    public ApiResponse updateCurrentUserAvatar(MultipartFile multipartFile) throws IOException {
        log.debug("UserService | updateCurrentUserAvatar");
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = findByEmail(userEmail)
                .orElseThrow(() ->  new NotFoundException("This email not exist"));

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            log.debug("UserService | updateCurrentUserAvatar | file name: " + fileName);

            user.setAvatar("/user-photos/" + user.getId() + "/" + fileName);
            User savedUser = userRepository.save(user);

            String uploadDir = "user-photos/" + savedUser.getId();

            log.debug("UserService | updateCurrentUserAvatar | savedUser : " + savedUser.toString());
            log.debug("UserService | updateCurrentUserAvatar | uploadDir : " + uploadDir);

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

            return new ApiResponse(true, "Image saved successfully");

        }
        else {
            return new ApiResponse(false, "Image not found");
        }
    }

    private VerifyAccountResponse CheckVerifyCode(VerifyAccountRequest request, VerificationCode code) {
        code.setCreateDate(new Date());

        if (code.getCode().equals(request.getUserCode())) {
            log.debug("UserService | VerifyAccount | account verified");

            code.setTempVerify(0);
            code.getUser().setEnable(true);
            verificationCodeService.save(code);
            return new VerifyAccountResponse("Account verified successfully", code.getUser().getEmail());
        }
        else {
            log.debug("UserService | VerifyAccount | account not verified");

            code.setTempVerify(code.getTempVerify() + 1);
            if (code.getTempVerify() == 5) {
                log.debug("UserService | VerifyAccount | account blocked");

                code.getUser().setAccountNonLocked(false);
                verificationCodeService.save(code);
                return new VerifyAccountResponse("Your code is incorrect, and your account blocked for 1 hour", code.getUser().getEmail());
            }
            verificationCodeService.save(code);
            return new VerifyAccountResponse("Your code is incorrect", code.getUser().getEmail());
        }
    }

}
