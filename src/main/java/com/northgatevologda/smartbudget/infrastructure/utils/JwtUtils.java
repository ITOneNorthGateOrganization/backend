package com.northgatevologda.smartbudget.infrastructure.utils;

import com.northgatevologda.smartbudget.infrastructure.web.entities.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Component class for handling JWT token generation and validation.
 */
@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${smartbudget.app.jwtSecret}")
    private String jwtSecret;

    @Value("${smartbudget.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * Generate a JWT token from the provided authentication details.
     *
     * @param authentication the Authentication object containing user details
     * @return the generated JWT token
     */
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return generateTokenFromUsername(userPrincipal.getUsername());
    }

    /**
     * Generate a JWT token based on the provided username.
     *
     * @param username the username for which the token is generated
     * @return the generated JWT token
     */
    public String generateTokenFromUsername(String username) {
        Date expirationDate = new Date((new Date()).getTime() + jwtExpirationMs);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(getSecretKey())
                .compact();
    }

    /**
     * Get the username from a JWT token.
     *
     * @param token the JWT token to parse
     * @return the username extracted from the token
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Validate the provided JWT token.
     *
     * @param authToken the JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}
