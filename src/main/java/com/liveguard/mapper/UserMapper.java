package com.liveguard.mapper;

import com.liveguard.domain.User;
import com.liveguard.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserMapper {

    public static User UserDTOToUser(UserDTO userDTO) {
        log.debug("UserMapper | UserDTOToUser | " + userDTO.toString());

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setPassword(userDTO.getPassword());
        user.setPhone(userDTO.getPhone());
        user.setAddress(userDTO.getAddress());
        user.setDob(userDTO.getDob());

        log.debug("UserMapper | UserDTOToUser | " + user.toString());

        return user;
    }
}
