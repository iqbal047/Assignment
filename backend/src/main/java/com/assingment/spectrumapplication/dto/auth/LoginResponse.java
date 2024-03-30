package com.assingment.spectrumapplication.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String username;
    private List<String> roles;
    private List<String> permissions;
    private String accessToken;
    private String status;
    private String message;
}
