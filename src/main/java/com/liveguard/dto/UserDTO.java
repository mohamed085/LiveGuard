package com.liveguard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@AllArgsConstructor
public class UserDTO {
    private String email;
    private String name;
    private String password;
    private String phone;
    private String address;
    private Date dob;

    @JsonProperty("avatar_file")
    private MultipartFile avatarFile;

    private String avatar;
}
