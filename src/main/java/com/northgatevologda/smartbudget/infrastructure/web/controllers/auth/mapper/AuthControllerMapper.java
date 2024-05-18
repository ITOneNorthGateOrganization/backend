package com.northgatevologda.smartbudget.infrastructure.web.controllers.auth.mapper;

import com.northgatevologda.smartbudget.application.service.refreshtoken.dto.AuthenticatedUserDTO;
import com.northgatevologda.smartbudget.application.service.user.dto.UserAuthenticationDTO;
import com.northgatevologda.smartbudget.application.service.user.dto.UserRegistrationDTO;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.auth.dto.JwtResponse;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.auth.dto.LoginRequest;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.auth.dto.SignupRequest;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for mapping different request and response objects related to authentication.
 */
@Component
public class AuthControllerMapper {

    /**
     * Maps LoginRequest to UserAuthenticationDTO.
     *
     * @param loginRequest The login request object.
     * @return UserAuthenticationDTO containing the username and password from the login request.
     */
    public UserAuthenticationDTO toUserAuthenticationDTO(LoginRequest loginRequest) {
        return UserAuthenticationDTO.builder()
                .username(loginRequest.getUsername())
                .password(loginRequest.getPassword())
                .build();
    }

    /**
     * Maps AuthenticatedUserDTO to JwtResponse.
     *
     * @param authenticatedUserDTO The authenticated user DTO.
     * @return JwtResponse containing the token, id, username, email, and roles from the authenticated user DTO.
     */
    public JwtResponse toJwtResponse(AuthenticatedUserDTO authenticatedUserDTO) {
        return JwtResponse.builder()
                .token(authenticatedUserDTO.getToken())
                .refreshToken(authenticatedUserDTO.getRefreshToken())
                .id(authenticatedUserDTO.getId())
                .username(authenticatedUserDTO.getUsername())
                .email(authenticatedUserDTO.getEmail())
                .roles(authenticatedUserDTO.getRoles())
                .build();
    }

    /**
     * Maps SignupRequest to UserRegistrationDTO.
     *
     * @param signUpRequest The signup request object.
     * @return UserRegistrationDTO containing the username, password, and email from the signup request.
     */
    public UserRegistrationDTO toUserRegistrationDTO(SignupRequest signUpRequest) {
        return UserRegistrationDTO.builder()
                .username(signUpRequest.getUsername())
                .password(signUpRequest.getPassword())
                .email(signUpRequest.getEmail())
                .build();
    }
}
