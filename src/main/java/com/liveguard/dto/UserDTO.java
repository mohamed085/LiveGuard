package com.liveguard.dto;

import com.liveguard.domain.Chip;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String email;
    private String name;
    private String password;
    private String phone;
    private String address;
    private Date dob;
    private String avatar;
    private Set<Chip> chips;
}
