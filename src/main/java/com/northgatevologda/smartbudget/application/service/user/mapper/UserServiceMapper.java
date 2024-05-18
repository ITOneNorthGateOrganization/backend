package com.northgatevologda.smartbudget.application.service.user.mapper;

import com.northgatevologda.smartbudget.application.service.role.mapper.RoleServiceMapper;
import com.northgatevologda.smartbudget.application.service.user.dto.UserDTO;
import com.northgatevologda.smartbudget.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for mapping User entities to UserDTOs.
 */
@RequiredArgsConstructor
@Component
public class UserServiceMapper {
    private final RoleServiceMapper roleServiceMapper;

    /**
     * Maps UserDTO to User entity.
     *
     * @param userDTO The UserDTO to map.
     * @return User entity containing the user details and roles from the UserDTO.
     */
    public User toUser(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .email(userDTO.getEmail())
                .createdAt(userDTO.getCreatedAt())
                .password(userDTO.getPassword())
                .username(userDTO.getUsername())
                .roles(roleServiceMapper.toEntityList(userDTO.getRoles()))
                .build();
    }

    /**
     * Maps User entity to UserDTO.
     *
     * @param user The User entity to map.
     * @return UserDTO containing the user details and roles.
     */
    public UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(roleServiceMapper.toDTOList(user.getRoles()))
                .build();
    }
}
