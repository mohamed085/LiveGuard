package com.liveguard.controller;

import com.liveguard.domain.User;
import com.liveguard.domain.VerificationCode;
import com.liveguard.dto.UserDTO;
import com.liveguard.exciptions.BadRequestException;
import com.liveguard.exciptions.EmailAlreadyExistsException;
import com.liveguard.exciptions.UsernameAlreadyExistsException;
import com.liveguard.payload.*;
import com.liveguard.service.UserService;
import com.liveguard.service.VerificationCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final UserService userService;
    private final VerificationCodeService verificationCodeService;

    public AuthController(UserService userService, VerificationCodeService verificationCodeService) {
        this.userService = userService;
        this.verificationCodeService = verificationCodeService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody UserDTO userDTO) {
        log.debug("AuthController | Register | user try to register: " + userDTO.getEmail());

        User user = userService.register(userDTO);

        VerificationCode code = verificationCodeService.findByUserId(user.getId());

        return ResponseEntity
                .ok()
                .body(new RegisterResponse(code.getCode(), user.getEmail(), "Account created successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        log.debug("AuthController | Login | user try to login: " + loginRequest.getEmail());
        String token = userService.login(loginRequest.getEmail(), loginRequest.getPassword());

        return ResponseEntity
                .ok()
                .body(new LoginResponse(loginRequest.getEmail(), token));
    }

    @PostMapping("/verify-account")
    public ResponseEntity<?> verifyAccount(@RequestBody VerifyAccountRequest verifyAccountRequest) {
        log.debug("AuthController | verifyAccount | user try to verifyAccount: " + verifyAccountRequest.getUserEmail());

        return ResponseEntity
                .ok()
                .body(userService.verifyAccount(verifyAccountRequest));
    }

    @PostMapping("/resend-verify-mail")
    public ResponseEntity<RegisterResponse> resendVerifyMail(@RequestBody ResendVerifyMailRequest email) {
        log.debug("AuthController | resendVerifyMail | user try to resendVerifyMail: " + email.getUserEmail());

        User user = userService.resendVerifyAccount(email);
        VerificationCode code = verificationCodeService.findByUserId(user.getId());

        return ResponseEntity
                .ok()
                .body(new RegisterResponse(code.getCode(), user.getEmail(), "Mail resend successfully"));
    }
}
