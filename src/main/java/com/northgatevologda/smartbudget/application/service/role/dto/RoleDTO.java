package com.northgatevologda.smartbudget.application.service.role.dto;

import com.northgatevologda.smartbudget.domain.enums.ERole;
import lombok.Builder;
import lombok.Data;

/**
 * DTO for {@link com.northgatevologda.smartbudget.domain.model.Role}
 */
@Data
@Builder
public class RoleDTO {
    private Long id;
    private ERole name;
}