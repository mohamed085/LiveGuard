package com.liveguard.service;

import com.liveguard.domain.User;
import com.liveguard.dto.UserDTO;
import com.liveguard.payload.ResendVerifyMailRequest;
import com.liveguard.payload.VerifyAccountRequest;
import com.liveguard.payload.VerifyAccountResponse;

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

    public User resendVerifyAccount(ResendVerifyMailRequest request);

}
