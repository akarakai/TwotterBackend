package com.akaci.twotterbackend.security.authentication.jwt;

import com.akaci.twotterbackend.application.service.crud.AccountCrudService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.ParseException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LogManager.getLogger(JwtFilter.class);
    private static final String COOKIE_NAME = "jwt-token";
    private final JwtUtil jwtUtil;
    private final AccountCrudService accountCrudService;

    public JwtFilter(AccountCrudService accountCrudService, JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.accountCrudService = accountCrudService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        String authorizationHeader = request.getHeader("Authorization");

        // Skip public API paths and login endpoint
        if (uri.contains("api/public") || uri.contains("api/auth/login")) {
            filterChain.doFilter(request, response);
            return; // Prevent further processing
        }

        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // No Basic Auth token, proceed to check for JWT in cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(COOKIE_NAME)) {
                    String jwtToken = cookie.getValue();

                    try {
                        // Validate and extract authentication from JWT
                        Authentication authentication = jwtUtil.getAuthenticationFromJwt(jwtToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        if (authentication != null && authentication.isAuthenticated()) {
                            accountCrudService.updateLastLoggedIn(authentication.getName());
                        }
                        filterChain.doFilter(request, response); // Proceed with the filter chain
                        return; // Stop further execution after the filter chain
                    } catch (ParseException | BadJOSEException | JOSEException e) {
                        LOGGER.error("JWT validation failed", e);
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                        return; // Reject the request with Unauthorized status
                    }
                }
            }
        }

        // No JWT cookie found, reject with 401 Unauthorized for non-public paths
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication required");
    }
}


