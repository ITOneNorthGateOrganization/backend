package com.northgatevologda.smartbudget.infrastructure.web.controllers.auth.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Class representing a response containing the access token and refresh token for token refresh.
 */
@Data
@Builder
public class TokenRefreshResponse {
    private String accessToken;
    private String refreshToken;
    @Builder.Default
    private String tokenType = "Bearer";
}
