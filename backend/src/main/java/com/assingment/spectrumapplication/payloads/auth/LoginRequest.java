package com.assingment.spectrumapplication.payloads.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "Username address cannot be empty")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    private String password;

}
