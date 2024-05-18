package com.northgatevologda.smartbudget.infrastructure.web.controllers.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Class representing a request for user login.
 */
@Data
public class LoginRequest {
    @NotNull(message = "Username cannot be null")
    private String username;
    @NotNull(message = "Password cannot be null")
    public String password;
}
