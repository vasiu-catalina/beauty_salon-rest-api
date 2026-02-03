package com.vasiu_catalina.beauty_salon.security.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.vasiu_catalina.beauty_salon.security.manager.EmployeeAuthenticationManager;
import com.vasiu_catalina.beauty_salon.security.service.SecurityService;

import jakarta.servlet.FilterChain;

@ExtendWith(MockitoExtension.class)
class EmployeeAuthenticationFilterTest {

    @Mock
    private EmployeeAuthenticationManager authenticationManager;

    @Mock
    private SecurityService securityService;

    @Mock
    private FilterChain filterChain;

    @Test
    void attemptAuthenticationReadsCredentials() {
        EmployeeAuthenticationFilter filter = new EmployeeAuthenticationFilter(authenticationManager, securityService);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContentType("application/json");
        request.setContent("{\"email\":\"user@example.com\",\"password\":\"secret\"}"
                .getBytes(StandardCharsets.UTF_8));
        MockHttpServletResponse response = new MockHttpServletResponse();

        Authentication expected = new UsernamePasswordAuthenticationToken("user@example.com", "secret");
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(expected);

        Authentication result = filter.attemptAuthentication(request, response);

        assertSame(expected, result);
        ArgumentCaptor<Authentication> captor = ArgumentCaptor.forClass(Authentication.class);
        verify(authenticationManager).authenticate(captor.capture());
        assertEquals("user@example.com", captor.getValue().getName());
        assertEquals("secret", captor.getValue().getCredentials());
    }

    @Test
    void attemptAuthenticationWithInvalidBodyThrowsRuntimeException() {
        EmployeeAuthenticationFilter filter = new EmployeeAuthenticationFilter(authenticationManager, securityService);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent(new byte[0]);

        assertThrows(RuntimeException.class, () -> filter.attemptAuthentication(request, new MockHttpServletResponse()));
    }

    @Test
    void unsuccessfulAuthenticationDelegatesToSecurityService() throws Exception {
        EmployeeAuthenticationFilter filter = new EmployeeAuthenticationFilter(authenticationManager, securityService);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        BadCredentialsException failed = new BadCredentialsException("bad");

        filter.unsuccessfulAuthentication(request, response, failed);

        verify(securityService).unsuccessfulAuthentication(response, failed);
    }

    @Test
    void successfulAuthenticationDelegatesToSecurityService() throws Exception {
        EmployeeAuthenticationFilter filter = new EmployeeAuthenticationFilter(authenticationManager, securityService);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Authentication auth = new UsernamePasswordAuthenticationToken("user@example.com", "secret");

        filter.successfulAuthentication(request, response, filterChain, auth);

        verify(securityService).successfulAuthentication(response, "user@example.com", "EMPLOYEE");
    }
}
