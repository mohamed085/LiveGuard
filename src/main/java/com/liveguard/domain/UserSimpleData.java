package com.liveguard.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSimpleData {
    private Long id;
    private String email;
    private String name;
    private String avatar;
}
