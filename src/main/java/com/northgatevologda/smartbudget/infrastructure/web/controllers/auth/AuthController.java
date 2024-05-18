package com.northgatevologda.smartbudget.infrastructure.web.controllers.auth;

import com.northgatevologda.smartbudget.application.service.refreshtoken.dto.AuthenticatedUserDTO;
import com.northgatevologda.smartbudget.application.service.user.dto.UserAuthenticationDTO;
import com.northgatevologda.smartbudget.application.service.user.dto.UserRegistrationDTO;
import com.northgatevologda.smartbudget.domain.ports.in.UserService;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.auth.dto.*;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.auth.mapper.AuthControllerMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthControllerMapper mapper;
    private final UserService userService;

    @PostMapping("/signIn")
    public ResponseEntity<JwtResponse> signIn(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("Signing in user with username: {}", loginRequest.getUsername());
        UserAuthenticationDTO userAuthenticationDTO = mapper.toUserAuthenticationDTO(loginRequest);
        AuthenticatedUserDTO authenticatedUserDTO = userService.signIn(userAuthenticationDTO);
        return ResponseEntity.ok(mapper.toJwtResponse(authenticatedUserDTO));
    }

    @PostMapping("/signUp")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest signUpRequest) {
        logger.info("Signing up new user with username: {}", signUpRequest.getUsername());
        UserRegistrationDTO userRegistrationDTO = mapper.toUserRegistrationDTO(signUpRequest);
        userService.signup(userRegistrationDTO);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        logger.info("Refreshing token with refresh token: {}", request.getRefreshToken());
        String requestRefreshToken = request.getRefreshToken();
        String newAccessToken = userService.renewAccessToken(requestRefreshToken);
        TokenRefreshResponse response = TokenRefreshResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(requestRefreshToken)
                .build();
        return ResponseEntity.ok(response);
    }
}