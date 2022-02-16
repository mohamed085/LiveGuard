package com.liveguard.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyAccountRequest {

    @JsonProperty("user_code")
    private String userCode;

    @JsonProperty("user_email")
    private String userEmail;
}
