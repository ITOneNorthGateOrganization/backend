package com.northgatevologda.smartbudget.application.service.refreshtoken.mapper;

import com.northgatevologda.smartbudget.application.service.refreshtoken.dto.RefreshTokenDTO;
import com.northgatevologda.smartbudget.application.service.user.mapper.UserServiceMapper;
import com.northgatevologda.smartbudget.domain.model.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for mapping RefreshToken entities to RefreshTokenDTOs.
 */
@RequiredArgsConstructor
@Component
public class RefreshServiceMapper {
    private final UserServiceMapper userServiceMapper;

    /**
     * Maps RefreshToken entity to RefreshTokenDTO.
     *
     * @param refreshToken The RefreshToken entity to map.
     * @return RefreshTokenDTO containing the ID, token, and expiry date of the refresh token.
     */
    public RefreshTokenDTO toDTO(RefreshToken refreshToken) {
        return RefreshTokenDTO.builder()
                .id(refreshToken.getId())
                .token(refreshToken.getToken())
                .expiryDate(refreshToken.getExpiryDate())
                .userDTO(userServiceMapper.toUserDTO(refreshToken.getUser()))
                .build();
    }
}
