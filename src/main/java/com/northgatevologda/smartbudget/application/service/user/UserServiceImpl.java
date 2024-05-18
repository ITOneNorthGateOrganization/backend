package com.northgatevologda.smartbudget.application.service.user;

import com.northgatevologda.smartbudget.application.service.refreshtoken.dto.AuthenticatedUserDTO;
import com.northgatevologda.smartbudget.application.service.user.dto.UserAuthenticationDTO;
import com.northgatevologda.smartbudget.application.service.user.dto.UserRegistrationDTO;
import com.northgatevologda.smartbudget.domain.enums.ERole;
import com.northgatevologda.smartbudget.domain.exception.BadRequestException;
import com.northgatevologda.smartbudget.domain.exception.NotFoundException;
import com.northgatevologda.smartbudget.domain.model.Category;
import com.northgatevologda.smartbudget.domain.model.RefreshToken;
import com.northgatevologda.smartbudget.domain.model.Role;
import com.northgatevologda.smartbudget.domain.model.User;
import com.northgatevologda.smartbudget.domain.ports.in.CategoryService;
import com.northgatevologda.smartbudget.domain.ports.in.RefreshTokenService;
import com.northgatevologda.smartbudget.domain.ports.in.RoleService;
import com.northgatevologda.smartbudget.domain.ports.in.UserService;
import com.northgatevologda.smartbudget.domain.ports.out.UserRepositoryPort;
import com.northgatevologda.smartbudget.infrastructure.utils.JwtUtils;
import com.northgatevologda.smartbudget.infrastructure.web.entities.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of {@link UserService}
 */
@Component
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final RoleService roleService;
    private final RefreshTokenService refreshTokenService;
    private final CategoryService categoryService;

    public UserServiceImpl(
            AuthenticationManager authenticationManager,
            UserRepositoryPort userRepositoryPort,
            PasswordEncoder encoder,
            JwtUtils jwtUtils,
            RoleService roleService,
            RefreshTokenService refreshTokenService,
            @Lazy CategoryService categoryService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepositoryPort = userRepositoryPort;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.roleService = roleService;
        this.refreshTokenService = refreshTokenService;
        this.categoryService = categoryService;
    }

    @Override
    public AuthenticatedUserDTO signIn(UserAuthenticationDTO userAuthenticationDTO) {
        logger.info("Signing in user with username: {}", userAuthenticationDTO.getUsername());
        Authentication authentication = authenticateUser(userAuthenticationDTO);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = generateJwtToken(authentication);
        UserDetailsImpl userDetails = extractUserDetailsFromAuthentication(authentication);
        User foundUser = findById(userDetails.getId());
        RefreshToken refreshToken = tryCreateRefreshToken(foundUser);
        List<String> roles = extractUserRoles(userDetails);
        return AuthenticatedUserDTO.builder()
                .token(jwt)
                .refreshToken(refreshToken.getToken())
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .roles(roles)
                .build();
    }

    private RefreshToken tryCreateRefreshToken(User user) {
        refreshTokenService.deleteRefreshTokenByUserId(user.getId());
        return refreshTokenService.createRefreshToken(user);
    }

    private Authentication authenticateUser(UserAuthenticationDTO userAuthenticationDTO) {
        logger.info("Authenticating user: {}", userAuthenticationDTO.getUsername());
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(userAuthenticationDTO.getUsername(),
                        userAuthenticationDTO.getPassword());
        return authenticationManager.authenticate(token);
    }

    private String generateJwtToken(Authentication authentication) {
        return jwtUtils.generateJwtToken(authentication);
    }

    private UserDetailsImpl extractUserDetailsFromAuthentication(Authentication authentication) {
        return (UserDetailsImpl) authentication.getPrincipal();
    }

    private List<String> extractUserRoles(UserDetailsImpl userDetails) {
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }

    @Override
    public void signup(UserRegistrationDTO userRegistrationDTO) {
        logger.info("Signing up new user with username: {}", userRegistrationDTO.getUsername());
        checkUserBeforeSignup(userRegistrationDTO);
        registerUser(userRegistrationDTO);
    }

    private void checkUserBeforeSignup(UserRegistrationDTO userRegistrationDTO) {
        if (existsByUsername(userRegistrationDTO.getUsername())) {
            String errorMessage = "Error: Username " + userRegistrationDTO.getUsername() + " is already taken!";
            logger.error(errorMessage);
            throw new BadRequestException(errorMessage);
        }

        if (existsByEmail(userRegistrationDTO.getEmail())) {
            String errorMessage = "Error: Email " + userRegistrationDTO.getEmail() + " is already in use!";
            logger.error(errorMessage);
            throw new BadRequestException(errorMessage);
        }
    }

    private void registerUser(UserRegistrationDTO userRegistrationDTO) {
        User user = User.builder()
                .username(userRegistrationDTO.getUsername())
                .email(userRegistrationDTO.getEmail())
                .password(encoder.encode(userRegistrationDTO.getPassword()))
                .createdAt(Instant.now())
                .roles(getRolesForUserForRegistration())
                .categories(getDefaultCategories())
                .build();
        save(user);
    }

    private Set<Role> getRolesForUserForRegistration() {
        Set<Role> roles = new HashSet<>();
        Role foundRole = roleService.findByName(ERole.ROLE_USER);
        roles.add(foundRole);
        return roles;
    }

    private List<Category> getDefaultCategories() {
        return categoryService.findCategoriesByForEveryone(true);
    }

    @Override
    public String renewAccessToken(String refreshToken) {
        logger.info("Renewing access token with refresh token: {}", refreshToken);
        RefreshToken foundRefreshToken = refreshTokenService.findByToken(refreshToken);
        RefreshToken verifiedRefreshToken = refreshTokenService.verifyExpiration(foundRefreshToken);
        User userFromRefreshToken = verifiedRefreshToken.getUser();
        return jwtUtils.generateTokenFromUsername(userFromRefreshToken.getUsername());
    }

    @Override
    public void save(User user) {
        logger.info("Saving user: {}", user);
        userRepositoryPort.save(user);
    }

    @Override
    public User findByUsername(String username) {
        logger.info("Finding user by username: {}", username);
        return userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found by username"));
    }

    @Override
    public User findById(Long id) {
        logger.info("Finding user by id: {}", id);
        return userRepositoryPort.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found by id"));
    }

    @Override
    public boolean existsByUsername(String username) {
        logger.info("Checking if user exists by username: {}", username);
        return userRepositoryPort.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        logger.info("Checking if user exists by email: {}", email);
        return userRepositoryPort.existsByEmail(email);
    }
}
