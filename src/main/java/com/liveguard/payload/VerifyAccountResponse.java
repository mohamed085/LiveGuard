package com.liveguard.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyAccountResponse {
    private String message;

    @JsonProperty("user_email")
    private String userEmail;}
