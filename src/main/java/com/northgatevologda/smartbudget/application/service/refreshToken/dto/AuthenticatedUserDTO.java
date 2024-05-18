package com.northgatevologda.smartbudget.application.service.refreshtoken.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Data class representing an authenticated user with authentication details.
 */
@Data
@Builder
public class AuthenticatedUserDTO {
    private String token;
    private String refreshToken;
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
}
