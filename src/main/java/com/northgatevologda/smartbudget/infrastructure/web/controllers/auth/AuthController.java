package com.northgatevologda.smartbudget.infrastructure.web.controllers.auth;

import com.northgatevologda.smartbudget.application.service.refreshtoken.dto.AuthenticatedUserDTO;
import com.northgatevologda.smartbudget.application.service.user.dto.UserAuthenticationDTO;
import com.northgatevologda.smartbudget.application.service.user.dto.UserRegistrationDTO;
import com.northgatevologda.smartbudget.domain.ports.in.UserService;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.auth.dto.*;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.auth.mapper.AuthControllerMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication Controller", description = "Controller for user authentication")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthControllerMapper mapper;
    private final UserService userService;

    @PostMapping(value = "/signIn", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Sign in a user",
            description = "Authenticate a user and return the JWT token if authentication is successful",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully authenticated the user and returned the JWT"),
                    @ApiResponse(responseCode = "400", description = "Invalid login request data"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - incorrect username or password"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<JwtResponse> signIn(
            @Parameter(description = "Login request with username and password", required = true)
            @Valid @RequestBody LoginRequest loginRequest
    ) {
        logger.info("Signing in user with username: {}", loginRequest.getUsername());
        UserAuthenticationDTO userAuthenticationDTO = mapper.toUserAuthenticationDTO(loginRequest);
        AuthenticatedUserDTO authenticatedUserDTO = userService.signIn(userAuthenticationDTO);
        return ResponseEntity.ok(mapper.toJwtResponse(authenticatedUserDTO));
    }

    @PostMapping(value = "/signUp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Sign up a new user",
            description = "Registers a new user with given details and returns a message upon successful registration",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User registered successfully!"),
                    @ApiResponse(responseCode = "400", description = "Invalid registration data provided"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<String> signup(
            @Parameter(description = "Signup request with user registration details", required = true)
            @Valid @RequestBody SignupRequest signUpRequest
    ) {
        logger.info("Signing up new user with username: {}", signUpRequest.getUsername());
        UserRegistrationDTO userRegistrationDTO = mapper.toUserRegistrationDTO(signUpRequest);
        userService.signup(userRegistrationDTO);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping(value = "/refreshToken", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Refresh authentication token",
            description = "Provides a new access token using the supplied refresh token if it is valid",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully refreshed the authentication token"),
                    @ApiResponse(responseCode = "400", description = "Invalid refresh token"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized or expired refresh token"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<TokenRefreshResponse> refreshToken(
            @Parameter(description = "Token refresh request with refresh token", required = true)
            @Valid @RequestBody TokenRefreshRequest request
    ) {
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