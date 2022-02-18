package com.liveguard.controller;

import com.liveguard.dto.UserDTO;
import com.liveguard.payload.ApiResponse;
import com.liveguard.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/account")
public class AccountController {

    private final UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<UserDTO> userInfo() {
        log.debug("AccountController | userInfo");

        UserDTO userDTO = userService.userAccount();
        return ResponseEntity
                .ok()
                .body(userDTO);
    }

    @PutMapping("")
    public ResponseEntity<UserDTO> updateUserInfo(@RequestBody UserDTO userDTO) {
        log.debug("AccountController | updateUserInfo");
        UserDTO returnUserDTO = userService.updateCurrentUser(userDTO);

        return ResponseEntity
                .ok()
                .body(returnUserDTO);
    }

    @PutMapping("/avatar")
    public ResponseEntity<ApiResponse> updateUserAvatar(@RequestParam("file") MultipartFile multipartFile) {
        log.debug("AccountController | updateUserAvatar");

        try {
            ApiResponse apiResponse = userService.updateCurrentUserAvatar(multipartFile);
            return ResponseEntity
                    .ok()
                    .body(apiResponse);
        } catch (IOException e) {
            return ResponseEntity
                    .ok()
                    .body(new ApiResponse(false, "Failed to save photo"));
        }
    }
}
