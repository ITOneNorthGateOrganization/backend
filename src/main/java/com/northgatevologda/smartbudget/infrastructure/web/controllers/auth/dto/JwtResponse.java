package com.northgatevologda.smartbudget.infrastructure.web.controllers.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Class representing a response containing JWT authentication details.
 */
@Data
@Builder
public class JwtResponse {
    private String token;
    @Builder.Default
    private String type = "Bearer";
    private String refreshToken;
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
}
