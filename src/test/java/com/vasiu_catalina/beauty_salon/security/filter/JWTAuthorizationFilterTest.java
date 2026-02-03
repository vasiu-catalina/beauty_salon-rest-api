package com.vasiu_catalina.beauty_salon.security.filter;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vasiu_catalina.beauty_salon.security.service.SecurityService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class JWTAuthorizationFilterTest {

    @Mock
    private SecurityService securityService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Test
    void logoutRequestShortCircuits() throws Exception {
        when(securityService.isLogoutRequest(request)).thenReturn(true);

        JWTAuthorizationFilter filter = new JWTAuthorizationFilter(securityService);
        filter.doFilterInternal(request, response, filterChain);

        verify(securityService).logout(response);
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void nonBearerHeaderSkipsAuthentication() throws Exception {
        when(securityService.isLogoutRequest(request)).thenReturn(false);
        when(securityService.getHeader(request)).thenReturn("Basic abc");
        when(securityService.isBearerHeader("Basic abc")).thenReturn(false);

        JWTAuthorizationFilter filter = new JWTAuthorizationFilter(securityService);
        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(securityService, never()).authenticateUser("Basic abc");
    }

    @Test
    void bearerHeaderAuthenticates() throws Exception {
        when(securityService.isLogoutRequest(request)).thenReturn(false);
        when(securityService.getHeader(request)).thenReturn("Bearer token");
        when(securityService.isBearerHeader("Bearer token")).thenReturn(true);

        JWTAuthorizationFilter filter = new JWTAuthorizationFilter(securityService);
        filter.doFilterInternal(request, response, filterChain);

        verify(securityService).authenticateUser("Bearer token");
        verify(filterChain).doFilter(request, response);
    }
}
