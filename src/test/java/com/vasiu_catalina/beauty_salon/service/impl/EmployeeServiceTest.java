package com.vasiu_catalina.beauty_salon.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Optional;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vasiu_catalina.beauty_salon.entity.Employee;
import com.vasiu_catalina.beauty_salon.entity.Service;
import com.vasiu_catalina.beauty_salon.exception.employee.EmployeeAlreadyExistsException;
import com.vasiu_catalina.beauty_salon.exception.employee.EmployeeNotFoundException;
import com.vasiu_catalina.beauty_salon.exception.service.ServiceNotFoundException;
import com.vasiu_catalina.beauty_salon.repository.EmployeeRepository;
import com.vasiu_catalina.beauty_salon.repository.ServiceRepository;
import com.vasiu_catalina.beauty_salon.service.impl.EmployeeServiceImpl;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    public void testGetAllEmployees() {

        when(employeeRepository.findAll()).thenReturn(List.of(createEmployee(), createOtherEmployee()));

        List<Employee> result = employeeService.getAllEmployees();

        assertEquals(2, result.size());
        assertEquals("Catalina", result.get(0).getFirstName());
        assertEquals("nume@gmail.com", result.get(1).getEmail());
    }

    @Test
    public void testCreateEmployee() {
        Employee employee = createEmployee();
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee result = employeeService.createEmployee(employee);

        assertFullResult(result);
        verify(employeeRepository, times(1)).save(employee);

    }

    @Test
    public void testEmailAlreadyTaken() {
        Employee employee = createEmployee();
        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));

        EmployeeAlreadyExistsException exception = assertThrows(EmployeeAlreadyExistsException.class, () -> {
            employeeService.createEmployee(employee);
        });

        assertEmployeeAlreadyExistsException(exception, "Email");
        verify(employeeRepository, times(1)).findByEmail(employee.getEmail());
        verify(employeeRepository, never()).save(employee);
    }

    @Test
    public void testPhoneNumberAlreadyTaken() {
        Employee employee = createEmployee();
        when(employeeRepository.findByPhoneNumber(employee.getPhoneNumber())).thenReturn(Optional.of(employee));

        EmployeeAlreadyExistsException exception = assertThrows(EmployeeAlreadyExistsException.class, () -> {
            employeeService.createEmployee(employee);
        });

        assertEmployeeAlreadyExistsException(exception, "Phone number");
        verify(employeeRepository, times(1)).findByPhoneNumber(employee.getPhoneNumber());
        verify(employeeRepository, never()).save(employee);
    }

    @Test
    public void testGetEmployee() {

        Long employeeId = 1L;
        Employee employee = createEmployee();
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        Employee result = employeeService.getEmployee(employeeId);

        assertFullResult(result);
        verify(employeeRepository, times(1)).findById(employeeId);
    }

    @Test
    public void testEmployeeNotFound() {
        Long employeeId = 1L;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.getEmployee(employeeId);
        });

        assertEmployeeNotFoundException(exception, employeeId);
        verify(employeeRepository, times(1)).findById(employeeId);
    }

    @Test
    public void testUpdateEmployee() {
        Long employeeId = 1L;
        Employee existingEmployee = createOtherEmployee();
        Employee updatedEmployee = createEmployee();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

        Employee result = employeeService.updateEmployee(employeeId, updatedEmployee);

        assertFullResult(result);
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(1)).save(existingEmployee);
    }

    @Test
    public void testUpdateEmployeeEmailAlreadyExists() {
        Long employeeId = 1L;
        Employee existingEmployee = createOtherEmployee();
        Employee updatedEmployee = createEmployee();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.findByEmail(updatedEmployee.getEmail())).thenReturn(Optional.of(updatedEmployee));

        EmployeeAlreadyExistsException exception = assertThrows(EmployeeAlreadyExistsException.class, () -> {
            employeeService.updateEmployee(employeeId, updatedEmployee);
        });

        assertEmployeeAlreadyExistsException(exception, "Email");
        verify(employeeRepository, times(1)).findByEmail(updatedEmployee.getEmail());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    public void testUpdateEmployeePhoneAlreadyExists() {
        Long employeeId = 1L;
        Employee existingEmployee = createOtherEmployee();
        Employee updatedEmployee = createEmployee();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.findByEmail(updatedEmployee.getEmail())).thenReturn(Optional.empty());
        when(employeeRepository.findByPhoneNumber(updatedEmployee.getPhoneNumber())).thenReturn(Optional.of(updatedEmployee));

        EmployeeAlreadyExistsException exception = assertThrows(EmployeeAlreadyExistsException.class, () -> {
            employeeService.updateEmployee(employeeId, updatedEmployee);
        });

        assertEmployeeAlreadyExistsException(exception, "Phone number");
        verify(employeeRepository, times(1)).findByPhoneNumber(updatedEmployee.getPhoneNumber());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    public void testUpdateEmployeeWithSameEmailSkipsUniquenessChecks() {
        Long employeeId = 1L;
        Employee existingEmployee = createEmployee();
        Employee updatedEmployee = createEmployee();
        updatedEmployee.setFirstName("Updated");

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(existingEmployee);

        Employee result = employeeService.updateEmployee(employeeId, updatedEmployee);

        assertEquals("Updated", result.getFirstName());
        verify(employeeRepository, never()).findByEmail(any(String.class));
        verify(employeeRepository, never()).findByPhoneNumber(any(String.class));
    }

    @Test
    public void testUpdateEmployeeNotFound() {
        Long employeeId = 1L;
        Employee updatedEmployee = createEmployee();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.updateEmployee(employeeId, updatedEmployee);
        });

        assertEmployeeNotFoundException(exception, employeeId);
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    public void testGetEmployeeByEmail() {
        Employee employee = createEmployee();
        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));

        Employee result = employeeService.getEmployeeByEmail(employee.getEmail());

        assertEquals(employee, result);
        verify(employeeRepository, times(1)).findByEmail(employee.getEmail());
    }

    @Test
    public void testGetEmployeeByEmailNotFound() {
        when(employeeRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.getEmployeeByEmail("missing@example.com");
        });

        assertEmployeeNotFoundException(exception, null);
    }

    @Test
    public void testDeleteEmployee() {
        Long employeeId = 1L;
        Employee existingEmployee = createEmployee();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));

        employeeService.deleteEmployee(employeeId);

        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }

    @Test
    public void testdeleteEmployeeNotFound() {
        Long employeeId = 1L;

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.deleteEmployee(employeeId);
        });

        assertEmployeeNotFoundException(exception, employeeId);
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, never()).deleteById(employeeId);
    }

    @Test
    public void testGetServicesByEmployee() {

        Long employeeId = 1L;
        Employee employee = createEmployee();
        Set<Service> expectedServices = new HashSet<>(
                List.of(ServiceServiceTest.createService(), ServiceServiceTest.createOtherService()));
        employee.setServices(expectedServices);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        Set<Service> result = employeeService.getServicesByEmployee(employeeId);

        assertEquals(expectedServices, result);
        verify(employeeRepository, times(1)).findById(employeeId);

    }

    @Test
    public void testAddServiceToEmployee() {

        Long employeeId = 1L;
        Long serviceId = 1L;
        Employee employee = createEmployee();
        Service service = ServiceServiceTest.createService();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));
        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee result = employeeService.addServiceToEmployee(serviceId, employeeId);

        assertTrue(result.getServices().contains(service));
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(serviceRepository, times(1)).findById(serviceId);
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    public void testAddServiceToEmployeeServiceNotFound() {

        Long employeeId = 1L;
        Long serviceId = 1L;
        Employee employee = createEmployee();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.empty());

        ServiceNotFoundException exception = assertThrows(ServiceNotFoundException.class, () -> {
            employeeService.addServiceToEmployee(serviceId, employeeId);
        });

        ServiceServiceTest.assertServiceNotFoundException(exception, serviceId);

        verify(employeeRepository, times(1)).findById(employeeId);
        verify(serviceRepository, times(1)).findById(serviceId);
        verify(employeeRepository, never()).save(employee);
    }

    @Test
    public void testDeleteServiceFromEmployee() {

        Long employeeId = 1L;
        Long serviceId = 1L;
        Employee employee = createEmployee();
        Service service = ServiceServiceTest.createService();
        employee.getServices().add(service);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));
        when(employeeRepository.save(employee)).thenReturn(employee);

        employeeService.deleteServiceFromEmployee(serviceId, employeeId);

        assertFalse(employee.getServices().contains(service));
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(serviceRepository, times(1)).findById(serviceId);
        verify(employeeRepository, times(1)).save(employee);

    }

    @Test
    public void testDeleteServiceFromEmployeeServiceNotFound() {

        Long employeeId = 1L;
        Long serviceId = 1L;
        Employee employee = createEmployee();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.empty());

        ServiceNotFoundException exception = assertThrows(ServiceNotFoundException.class, () -> {
            employeeService.deleteServiceFromEmployee(serviceId, employeeId);
        });

        ServiceServiceTest.assertServiceNotFoundException(exception, serviceId);
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(serviceRepository, times(1)).findById(serviceId);
        verify(employeeRepository, never()).save(employee);
    }

    public void assertFullResult(Employee result) {
        assertNotNull(result);
        assertEquals("Catalina", result.getFirstName());
        assertEquals("Vasiu", result.getLastName());
        assertEquals("cata@gmail.com", result.getEmail());
        assertEquals("+4071xxxxxxx", result.getPhoneNumber());
        assertEquals("Str. Anonim Nr. 0", result.getAddress());
        assertEquals(LocalDate.of(2000, 1, 1), result.getBirthDate());
        assertEquals("Manichiurista", result.getSpecialization());
        assertEquals("employee", result.getRole());
        assertEquals(new BigDecimal(5000), result.getSalary());
    }

    public static void assertEmployeeNotFoundException(EmployeeNotFoundException exception, Long clientId) {
        EmployeeNotFoundException expected = new EmployeeNotFoundException(clientId);
        assertEquals(expected.getMessage(), exception.getMessage());
    }

    public static void assertEmployeeAlreadyExistsException(EmployeeAlreadyExistsException exception,
            String uniqueFieldName) {
        EmployeeAlreadyExistsException expected = new EmployeeAlreadyExistsException(uniqueFieldName);
        assertEquals(expected.getMessage(), exception.getMessage());
    }

    public static Employee createEmployee() {
        return new Employee("Catalina", "Vasiu", "cata@gmail.com", "+4071xxxxxxx", "Str. Anonim Nr. 0",
                LocalDate.of(2000, 1, 1), "Manichiurista", new BigDecimal(5000));
    }

    public static Employee createOtherEmployee() {
        return new Employee("Prenume", "Nume", "nume@gmail.com", "+4072xxxxxxx", "Str. Random Nr. 1",
                LocalDate.of(2001, 2, 2), "Specializare", new BigDecimal(2000));
    }

}
