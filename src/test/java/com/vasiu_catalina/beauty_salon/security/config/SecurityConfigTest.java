package com.vasiu_catalina.beauty_salon.security.config;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import com.vasiu_catalina.beauty_salon.security.manager.ClientAuthenticationManager;
import com.vasiu_catalina.beauty_salon.security.manager.EmployeeAuthenticationManager;
import com.vasiu_catalina.beauty_salon.security.service.SecurityService;

class SecurityConfigTest {

    @Test
    void filterChainBuildsWithConfiguredPaths() throws Exception {
        ClientAuthenticationManager clientManager = mock(ClientAuthenticationManager.class);
        EmployeeAuthenticationManager employeeManager = mock(EmployeeAuthenticationManager.class);
        SecurityService securityService = mock(SecurityService.class);

        when(securityService.getClientRegisterPath()).thenReturn("/api/v1/clients/register");
        when(securityService.getClientLoginPath()).thenReturn("/api/v1/clients/login");
        when(securityService.getEmployeeRegisterPath()).thenReturn("/api/v1/employees/register");
        when(securityService.getEmployeeLoginPath()).thenReturn("/api/v1/employees/login");
        when(securityService.getLogoutPath()).thenReturn("/api/v1/logout");

        SecurityConfig config = new SecurityConfig(clientManager, employeeManager, securityService);

        HttpSecurity http = mock(HttpSecurity.class, Answers.RETURNS_SELF);
        DefaultSecurityFilterChain filterChain = mock(DefaultSecurityFilterChain.class);
        when(http.build()).thenReturn(filterChain);

        when(http.csrf(any())).thenAnswer(invocation -> {
            @SuppressWarnings("unchecked")
            Customizer<CsrfConfigurer<HttpSecurity>> customizer =
                    (Customizer<CsrfConfigurer<HttpSecurity>>) invocation.getArgument(0);
            CsrfConfigurer<HttpSecurity> csrf = mock(CsrfConfigurer.class);
            when(csrf.disable()).thenReturn(http);
            customizer.customize(csrf);
            return http;
        });

        when(http.authorizeHttpRequests(any())).thenAnswer(invocation -> {
            @SuppressWarnings("unchecked")
            Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> customizer =
                    (Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>) invocation.getArgument(0);
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry =
                    mock(AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry.class, Answers.RETURNS_DEEP_STUBS);
            customizer.customize(registry);
            return http;
        });

        when(http.sessionManagement(any())).thenAnswer(invocation -> {
            @SuppressWarnings("unchecked")
            Customizer<SessionManagementConfigurer<HttpSecurity>> customizer =
                    (Customizer<SessionManagementConfigurer<HttpSecurity>>) invocation.getArgument(0);
            SessionManagementConfigurer<HttpSecurity> sessionManagement =
                    mock(SessionManagementConfigurer.class, Answers.RETURNS_DEEP_STUBS);
            customizer.customize(sessionManagement);
            return http;
        });

        SecurityFilterChain result = config.filterChain(http);

        assertSame(filterChain, result);
    }
}
