package com.northgatevologda.smartbudget.infrastructure.web.controllers.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Class representing a request for refreshing a token.
 */
@Data
public class TokenRefreshRequest {
    @NotNull
    private String refreshToken;
}
