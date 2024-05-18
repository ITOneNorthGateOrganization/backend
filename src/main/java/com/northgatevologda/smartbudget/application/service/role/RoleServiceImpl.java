package com.northgatevologda.smartbudget.application.service.role;

import com.northgatevologda.smartbudget.domain.enums.ERole;
import com.northgatevologda.smartbudget.domain.exception.NotFoundException;
import com.northgatevologda.smartbudget.domain.model.Role;
import com.northgatevologda.smartbudget.domain.ports.in.RoleService;
import com.northgatevologda.smartbudget.domain.ports.out.RoleRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link RoleService}
 */
@Component
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleRepositoryPort roleRepositoryPort;

    @Override
    public Role findByName(ERole name) {
        logger.info("Finding role by name: {}", name);
        return roleRepositoryPort.findByName(name)
                .orElseThrow(() -> new NotFoundException("The role was not found"));
    }
}
