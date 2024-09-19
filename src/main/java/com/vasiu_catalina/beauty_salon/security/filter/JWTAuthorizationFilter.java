package com.vasiu_catalina.beauty_salon.security.filter;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import com.vasiu_catalina.beauty_salon.security.service.SecurityService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final SecurityService securityService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        if (securityService.isLogoutRequest(request)) {
            securityService.logout(response);
            return;
        }

        String header = securityService.getHeader(request);

        if (!securityService.isBearerHeader(header)) {
            filterChain.doFilter(request, response);
            return;
        }

        securityService.authenticateUser(header);
        filterChain.doFilter(request, response);
    }



}
