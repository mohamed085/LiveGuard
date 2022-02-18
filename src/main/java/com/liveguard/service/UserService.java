package com.liveguard.service;

import com.liveguard.domain.User;
import com.liveguard.dto.UserDTO;
import com.liveguard.payload.ApiResponse;
import com.liveguard.payload.ResendVerifyMailRequest;
import com.liveguard.payload.VerifyAccountRequest;
import com.liveguard.payload.VerifyAccountResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface UserService {

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    Boolean userExistByEmail(String email);

    String login(String username, String password);

    User save(User user);

    User register(UserDTO userDTO);

    void deleteById(Long id);

    VerifyAccountResponse verifyAccount(VerifyAccountRequest request);

    User resendVerifyAccount(ResendVerifyMailRequest request);

    UserDTO userAccount();

    UserDTO updateCurrentUser(UserDTO userDTO);

    ApiResponse updateCurrentUserAvatar(MultipartFile multipartFile) throws IOException;
}
