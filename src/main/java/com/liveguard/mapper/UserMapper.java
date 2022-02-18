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

    public static UserDTO UserToUserDTO(User user) {
        log.debug("UserMapper | UserToUserDTO | " + user.toString());

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());
        userDTO.setPassword(null);
        userDTO.setPhone(user.getPhone());
        userDTO.setAddress(user.getAddress());
        userDTO.setDob(user.getDob());
        userDTO.setAvatar(user.getAvatar());
        userDTO.setChips(user.getChips());

        return userDTO;
    }
}
