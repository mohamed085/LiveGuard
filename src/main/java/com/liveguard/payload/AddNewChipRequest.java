package com.liveguard.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddNewChipRequest {
    private Long id;
    private String password;
}
