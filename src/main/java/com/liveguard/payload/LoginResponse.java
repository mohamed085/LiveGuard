package com.liveguard.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    @JsonProperty("user_email")
    private String userEmail;

    @JsonProperty("user_token")
    private String userToken;
}
