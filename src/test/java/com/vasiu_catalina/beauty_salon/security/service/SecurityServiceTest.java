package com.vasiu_catalina.beauty_salon.security.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vasiu_catalina.beauty_salon.service.impl.ClientServiceTest;
import com.vasiu_catalina.beauty_salon.service.impl.EmployeeServiceTest;
import com.vasiu_catalina.beauty_salon.entity.Client;
import com.vasiu_catalina.beauty_salon.entity.Employee;
import com.vasiu_catalina.beauty_salon.security.SecurityConstants;
import com.vasiu_catalina.beauty_salon.service.impl.ClientServiceImpl;
import com.vasiu_catalina.beauty_salon.service.impl.EmployeeServiceImpl;

import jakarta.servlet.http.Cookie;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {

    @Mock
    private ClientServiceImpl clientService;

    @Mock
    private EmployeeServiceImpl employeeService;

    private SecurityService securityService;

    @BeforeEach
    void setUp() throws Exception {
        securityService = new SecurityService(buildSecurityConstants());
        setField(securityService, "clientService", clientService);
        setField(securityService, "employeeService", employeeService);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void exposesConfiguredPaths() {
        assertEquals("/api/v1/clients/register", securityService.getClientRegisterPath());
        assertEquals("/api/v1/clients/login", securityService.getClientLoginPath());
        assertEquals("/api/v1/employees/register", securityService.getEmployeeRegisterPath());
        assertEquals("/api/v1/employees/login", securityService.getEmployeeLoginPath());
        assertEquals("/api/v1/logout", securityService.getLogoutPath());
    }

    @Test
    void logoutRequestIsDetected() {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/v1/logout");

        assertTrue(securityService.isLogoutRequest(request));
    }

    @Test
    void logoutRequestIsNotDetectedForDifferentMethod() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/logout");

        assertFalse(securityService.isLogoutRequest(request));
    }

    @Test
    void logoutRequestIsNotDetectedForDifferentPath() {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/v1/other");

        assertFalse(securityService.isLogoutRequest(request));
    }

    @Test
    void bearerHeaderDetectionWorks() {
        assertTrue(securityService.isBearerHeader("Bearer token"));
    }

    @Test
    void bearerHeaderDetectionFailsForNull() {
        assertFalse(securityService.isBearerHeader(null));
    }

    @Test
    void bearerHeaderDetectionFailsForNonBearer() {
        assertFalse(securityService.isBearerHeader("Basic token"));
    }

    @Test
    void getHeaderReadsAuthorization() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");

        assertEquals("Bearer token", securityService.getHeader(request));
    }

    @Test
    void successfulAuthenticationAddsCookie() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();

        securityService.successfulAuthentication(response, "client@example.com", "CLIENT");

        Cookie cookie = response.getCookies()[0];
        assertEquals("jwt", cookie.getName());
        assertEquals("/", cookie.getPath());
        assertTrue(cookie.isHttpOnly());
        assertEquals(7 * 24 * 3600, cookie.getMaxAge());
        assertNotNull(cookie.getValue());
    }

    @Test
    void unsuccessfulAuthenticationWritesResponse() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();

        securityService.unsuccessfulAuthentication(response, new BadCredentialsException("Bad credentials"));

        assertEquals(401, response.getStatus());
        assertEquals("Bad credentials", response.getContentAsString());
    }

    @Test
    void logoutClearsCookie() {
        MockHttpServletResponse response = new MockHttpServletResponse();

        securityService.logout(response);

        Cookie cookie = response.getCookies()[0];
        assertEquals("jwt", cookie.getName());
        assertEquals(0, cookie.getMaxAge());
    }

    @Test
    void authenticateUserWithClientRoleSetsSecurityContext() throws Exception {
        String email = "client@example.com";
        Client client = ClientServiceTest.createClient();
        client.setId(42L);
        client.setEmail(email);
        when(clientService.getClientByEmail(email)).thenReturn(client);

        String token = issueToken(email, "CLIENT");

        securityService.authenticateUser("Bearer " + token);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertEquals(email, authentication.getName());
        assertEquals(42L, authentication.getDetails());
        assertTrue(authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_CLIENT")));
    }

    @Test
    void authenticateUserWithEmployeeRoleSetsSecurityContext() throws Exception {
        String email = "employee@example.com";
        Employee employee = EmployeeServiceTest.createEmployee();
        employee.setId(7L);
        employee.setEmail(email);
        when(employeeService.getEmployeeByEmail(email)).thenReturn(employee);

        String token = issueToken(email, "EMPLOYEE");

        securityService.authenticateUser("Bearer " + token);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertEquals(email, authentication.getName());
        assertEquals(7L, authentication.getDetails());
        assertTrue(authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_EMPLOYEE")));
    }

    @Test
    void authenticateUserWithInvalidRoleThrows() throws Exception {
        String token = issueToken("user@example.com", "ADMIN");

        assertThrows(com.vasiu_catalina.beauty_salon.security.exception.BadCredentialsException.class,
                () -> securityService.authenticateUser("Bearer " + token));
    }

    private String issueToken(String email, String role) throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        securityService.successfulAuthentication(response, email, role);
        return response.getCookies()[0].getValue();
    }

    private static SecurityConstants buildSecurityConstants() {
        SecurityConstants constants = new SecurityConstants();

        SecurityConstants.Jwt jwt = new SecurityConstants.Jwt();
        jwt.setAuthorization("Authorization");
        jwt.setBearer("Bearer");
        jwt.setSecret("secret");
        jwt.setToken_expiration_days(1);
        constants.setJwt(jwt);

        SecurityConstants.Cookie cookie = new SecurityConstants.Cookie();
        cookie.setName("jwt");
        cookie.setPath("/");
        cookie.setHttponly(true);
        cookie.setSecure(false);
        cookie.setExpiration_days(7);
        constants.setCookie(cookie);

        SecurityConstants.Path path = new SecurityConstants.Path();
        path.setClient_register("/clients/register");
        path.setClient_login("/clients/login");
        path.setEmployee_register("/employees/register");
        path.setEmployee_login("/employees/login");
        path.setLogout("/logout");
        constants.setPath(path);

        return constants;
    }

    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
