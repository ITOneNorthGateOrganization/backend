package com.northgatevologda.smartbudget.application.service.user.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Data class representing user authentication details.
 */
@Data
@Builder
public class UserAuthenticationDTO {
    private String username;
    private String password;
}
