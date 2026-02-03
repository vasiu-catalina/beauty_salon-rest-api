package com.vasiu_catalina.beauty_salon.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;

import com.vasiu_catalina.beauty_salon.service.impl.ClientServiceTest;
import com.vasiu_catalina.beauty_salon.service.impl.EmployeeServiceTest;
import com.vasiu_catalina.beauty_salon.entity.Client;
import com.vasiu_catalina.beauty_salon.entity.Employee;
import com.vasiu_catalina.beauty_salon.service.IRegisterService;

@ExtendWith(MockitoExtension.class)
class RegisterControllerTest {

    @Mock
    private IRegisterService registerService;

    @InjectMocks
    private RegisterController registerController;

    @Test
    void registerClientReturnsCreated() throws Exception {
        Client client = ClientServiceTest.createClient();
        MockHttpServletResponse response = new MockHttpServletResponse();

        ResponseEntity<HttpStatus> result = registerController.registerClient(client, response);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        verify(registerService).registerClient(client, response);
    }

    @Test
    void registerEmployeeReturnsCreated() throws Exception {
        Employee employee = EmployeeServiceTest.createEmployee();
        MockHttpServletResponse response = new MockHttpServletResponse();

        ResponseEntity<HttpStatus> result = registerController.registerEmployee(employee, response);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        verify(registerService).registerEmployee(employee, response);
    }
}
