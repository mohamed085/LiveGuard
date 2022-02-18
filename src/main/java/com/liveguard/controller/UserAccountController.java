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
public class UserAccountController {

    private final UserService userService;

    public UserAccountController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<UserDTO> userInfo() {
        log.debug("UserAccountController | userInfo");

        UserDTO userDTO = userService.userAccount();
        return ResponseEntity
                .ok()
                .body(userDTO);
    }

    @PutMapping("")
    public ResponseEntity<UserDTO> updateUserInfo(@RequestBody UserDTO userDTO) {
        log.debug("UserAccountController | updateUserInfo");
        UserDTO returnUserDTO = userService.updateCurrentUser(userDTO);

        return ResponseEntity
                .ok()
                .body(returnUserDTO);
    }

    @PutMapping("/avatar")
    public ResponseEntity<ApiResponse> updateUserAvatar(@RequestParam("file") MultipartFile multipartFile) {
        log.debug("UserAccountController | updateUserAvatar");

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
