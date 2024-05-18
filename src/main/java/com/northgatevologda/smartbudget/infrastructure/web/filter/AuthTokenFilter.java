package com.northgatevologda.smartbudget.infrastructure.web.filter;

import com.northgatevologda.smartbudget.infrastructure.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter component for validating and handling JWT authentication tokens in requests.
 */
@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenFilter.class);

    public static final String HEADER_AUTH = "Authorization";
    public static final String HEADER_AUTH_PREFIX = "Bearer ";

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                authenticateUser(jwt, request);
            }
        } catch (Exception e) {
            LOGGER.error("Cannot set user authentication", e);
        } finally {
            filterChain.doFilter(request, response);
        }
    }

    private void authenticateUser(String jwt, HttpServletRequest request) {
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        LOGGER.info("User {} successfully authenticated", username);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(HEADER_AUTH);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(HEADER_AUTH_PREFIX)) {
            return headerAuth.substring(HEADER_AUTH_PREFIX.length());
        }
        return null;
    }
}
