package com.northgatevologda.smartbudget.infrastructure.web.controllers.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Class representing a request for user signup.
 */
@Data
public class SignupRequest {
    @NotNull
    private String username;
    @NotNull
    private String email;
    @NotNull
    private String password;
}
