package com.vasiu_catalina.beauty_salon.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.vasiu_catalina.beauty_salon.service.impl.ClientServiceTest;
import com.vasiu_catalina.beauty_salon.service.impl.EmployeeServiceTest;
import com.vasiu_catalina.beauty_salon.entity.Client;
import com.vasiu_catalina.beauty_salon.entity.Employee;
import com.vasiu_catalina.beauty_salon.security.exception.PasswordsDontMatchException;
import com.vasiu_catalina.beauty_salon.security.service.SecurityService;
import com.vasiu_catalina.beauty_salon.service.IClientService;
import com.vasiu_catalina.beauty_salon.service.IEmployeeService;

import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class RegisterServiceImplTest {

    @Mock
    private IClientService clientService;

    @Mock
    private IEmployeeService employeeService;

    @Mock
    private SecurityService securityService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private RegisterServiceImpl registerService;

    @Test
    void registerClientEncryptsPasswordAndAuthenticates() throws Exception {
        Client client = ClientServiceTest.createClient();
        client.setPassword("secret");
        client.setConfirmPassword("secret");
        when(passwordEncoder.encode("secret")).thenReturn("hashed");

        registerService.registerClient(client, response);

        assertEquals("hashed", client.getPassword());
        verify(clientService).createClient(client);
        verify(securityService).successfulAuthentication(response, client.getEmail(), "CLIENT");
    }

    @Test
    void registerClientThrowsWhenPasswordsDontMatch() {
        Client client = ClientServiceTest.createClient();
        client.setPassword("secret");
        client.setConfirmPassword("other");

        assertThrows(PasswordsDontMatchException.class,
                () -> registerService.registerClient(client, response));

        verifyNoInteractions(clientService);
    }

    @Test
    void registerEmployeeEncryptsPasswordAndAuthenticates() throws Exception {
        Employee employee = EmployeeServiceTest.createEmployee();
        employee.setPassword("secret");
        employee.setConfirmPassword("secret");
        when(passwordEncoder.encode("secret")).thenReturn("hashed");

        registerService.registerEmployee(employee, response);

        assertEquals("hashed", employee.getPassword());
        verify(employeeService).createEmployee(employee);
        verify(securityService).successfulAuthentication(response, employee.getEmail(), "CLIENT");
    }

    @Test
    void registerEmployeeThrowsWhenPasswordsDontMatch() {
        Employee employee = EmployeeServiceTest.createEmployee();
        employee.setPassword("secret");
        employee.setConfirmPassword("other");

        assertThrows(PasswordsDontMatchException.class,
                () -> registerService.registerEmployee(employee, response));

        verifyNoInteractions(employeeService);
    }
}
