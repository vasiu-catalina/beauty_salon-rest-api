package com.vasiu_catalina.beauty_salon.security.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.vasiu_catalina.beauty_salon.service.impl.EmployeeServiceTest;
import com.vasiu_catalina.beauty_salon.entity.Employee;
import com.vasiu_catalina.beauty_salon.security.exception.BadCredentialsException;
import com.vasiu_catalina.beauty_salon.service.IEmployeeService;

@ExtendWith(MockitoExtension.class)
class EmployeeAuthenticationManagerTest {

    @Mock
    private IEmployeeService employeeService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeAuthenticationManager authenticationManager;

    @Test
    void authenticateReturnsTokenWhenPasswordMatches() {
        Employee employee = EmployeeServiceTest.createEmployee();
        employee.setPassword("hashed");
        when(employeeService.getEmployeeByEmail("user@example.com")).thenReturn(employee);
        when(passwordEncoder.matches("plain", "hashed")).thenReturn(true);

        Authentication result = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("user@example.com", "plain"));

        assertEquals("user@example.com", result.getName());
        assertEquals("hashed", result.getCredentials());
    }

    @Test
    void authenticateThrowsWhenPasswordDoesNotMatch() {
        Employee employee = EmployeeServiceTest.createEmployee();
        employee.setPassword("hashed");
        when(employeeService.getEmployeeByEmail("user@example.com")).thenReturn(employee);
        when(passwordEncoder.matches("plain", "hashed")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("user@example.com", "plain")));
    }
}
