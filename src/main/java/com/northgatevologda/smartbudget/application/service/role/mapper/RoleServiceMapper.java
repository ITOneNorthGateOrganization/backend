package com.northgatevologda.smartbudget.application.service.role.mapper;

import com.northgatevologda.smartbudget.application.service.role.dto.RoleDTO;
import com.northgatevologda.smartbudget.domain.model.Role;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class is responsible for mapping Role entities to RoleDTOs.
 */
@Component
public class RoleServiceMapper {

    /**
     * Maps RoleDTO to Role entity.
     *
     * @param roleDTO The RoleDTO to map.
     * @return Role entity containing the ID and name from the RoleDTO.
     */
    public Role toEntity(RoleDTO roleDTO) {
        return Role.builder()
                .id(roleDTO.getId())
                .name(roleDTO.getName())
                .build();
    }

    /**
     * Maps a set of RoleDTOs to a set of Role entities.
     *
     * @param roleDTOs The set of RoleDTOs to map.
     * @return Set of Role entities containing the ID and name from each RoleDTO.
     */
    public Set<Role> toEntityList(Set<RoleDTO> roleDTOs) {
        return roleDTOs.stream().map(this::toEntity).collect(Collectors.toSet());
    }

    /**
     * Maps Role entity to RoleDTO.
     *
     * @param role The Role entity to map.
     * @return RoleDTO containing the ID and name of the role.
     */
    public RoleDTO toDTO(Role role) {
        return RoleDTO.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }

    /**
     * Maps a set of Role entities to a set of RoleDTOs.
     *
     * @param roles The set of Role entities to map.
     * @return Set of RoleDTOs containing the ID and name of each role in the set.
     */
    public Set<RoleDTO> toDTOList(Set<Role> roles) {
        return roles.stream().map(this::toDTO).collect(Collectors.toSet());
    }
}
