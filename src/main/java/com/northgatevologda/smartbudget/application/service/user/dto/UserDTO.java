package com.northgatevologda.smartbudget.application.service.user.dto;

import com.northgatevologda.smartbudget.application.service.role.dto.RoleDTO;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * DTO for {@link com.northgatevologda.smartbudget.domain.model.User}
 */
@Data
@Builder
public class UserDTO {
    private Long id;
    private Instant createdAt;
    private String email;
    private String password;
    private String username;
    @Builder.Default
    private Set<RoleDTO> roles = new HashSet<>();
}
