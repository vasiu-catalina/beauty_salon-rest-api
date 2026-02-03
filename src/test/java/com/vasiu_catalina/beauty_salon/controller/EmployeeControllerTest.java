package com.vasiu_catalina.beauty_salon.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.vasiu_catalina.beauty_salon.service.impl.EmployeeServiceTest;
import com.vasiu_catalina.beauty_salon.service.impl.ServiceServiceTest;
import com.vasiu_catalina.beauty_salon.entity.Employee;
import com.vasiu_catalina.beauty_salon.entity.Service;
import com.vasiu_catalina.beauty_salon.service.IEmployeeService;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private IEmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @Test
    void getAllEmployeesReturnsOk() {
        List<Employee> employees = List.of(EmployeeServiceTest.createEmployee(), EmployeeServiceTest.createOtherEmployee());
        when(employeeService.getAllEmployees()).thenReturn(employees);

        ResponseEntity<List<Employee>> response = employeeController.getAllEmployees();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(employees, response.getBody());
        verify(employeeService).getAllEmployees();
    }

    @Test
    void createEmployeeReturnsCreated() {
        Employee employee = EmployeeServiceTest.createEmployee();
        when(employeeService.createEmployee(employee)).thenReturn(employee);

        ResponseEntity<Employee> response = employeeController.createEmployee(employee);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(employee, response.getBody());
        verify(employeeService).createEmployee(employee);
    }

    @Test
    void getEmployeeReturnsOk() {
        Employee employee = EmployeeServiceTest.createEmployee();
        when(employeeService.getEmployee(1L)).thenReturn(employee);

        ResponseEntity<Employee> response = employeeController.getEmployee(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(employee, response.getBody());
        verify(employeeService).getEmployee(1L);
    }

    @Test
    void updateEmployeeReturnsOk() {
        Employee employee = EmployeeServiceTest.createEmployee();
        when(employeeService.updateEmployee(1L, employee)).thenReturn(employee);

        ResponseEntity<Employee> response = employeeController.updateEmployee(1L, employee);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(employee, response.getBody());
        verify(employeeService).updateEmployee(1L, employee);
    }

    @Test
    void deleteEmployeeReturnsNoContent() {
        ResponseEntity<HttpStatus> response = employeeController.deleteEmployee(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(employeeService).deleteEmployee(1L);
    }

    @Test
    void getServicesByEmployeeReturnsOk() {
        Set<Service> services = Set.of(ServiceServiceTest.createService());
        when(employeeService.getServicesByEmployee(1L)).thenReturn(services);

        ResponseEntity<Set<Service>> response = employeeController.getServicesByEmployee(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(services, response.getBody());
        verify(employeeService).getServicesByEmployee(1L);
    }

    @Test
    void addServiceToEmployeeReturnsOk() {
        Employee employee = EmployeeServiceTest.createEmployee();
        when(employeeService.addServiceToEmployee(2L, 1L)).thenReturn(employee);

        ResponseEntity<Employee> response = employeeController.addServiceToEmployee(1L, 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(employee, response.getBody());
        verify(employeeService).addServiceToEmployee(2L, 1L);
    }

    @Test
    void deleteServiceFromEmployeeReturnsNoContent() {
        ResponseEntity<HttpStatus> response = employeeController.deleteServiceFromEmployee(1L, 2L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(employeeService).deleteServiceFromEmployee(2L, 1L);
    }
}
