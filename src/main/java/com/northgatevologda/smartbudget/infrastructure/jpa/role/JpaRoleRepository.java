package com.northgatevologda.smartbudget.infrastructure.jpa.role;

import com.northgatevologda.smartbudget.domain.enums.ERole;
import com.northgatevologda.smartbudget.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}