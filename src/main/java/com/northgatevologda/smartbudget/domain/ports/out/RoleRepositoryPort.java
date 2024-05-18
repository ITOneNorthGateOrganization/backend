package com.northgatevologda.smartbudget.domain.ports.out;

import com.northgatevologda.smartbudget.domain.enums.ERole;
import com.northgatevologda.smartbudget.domain.model.Role;

import java.util.Optional;

/**
 * Interface for interacting with role data in the application.
 */
public interface RoleRepositoryPort {

    /**
     * Find a role by its name.
     *
     * @param name the name of the role to search for
     * @return an Optional containing the Role, if found
     */
    Optional<Role> findByName(ERole name);
}
