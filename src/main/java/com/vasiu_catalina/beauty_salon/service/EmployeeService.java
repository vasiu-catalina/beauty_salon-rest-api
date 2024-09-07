package com.vasiu_catalina.beauty_salon.service;

import java.util.Set;

import com.vasiu_catalina.beauty_salon.entity.Employee;
import com.vasiu_catalina.beauty_salon.entity.Service;

public interface EmployeeService {

    Set<Employee> getAllEmployees();

    Employee getEmployee(Long id);

    Employee createEmployee(Employee employee);

    Employee updateEmployee(Long id, Employee employee);

    void deleteEmployee(Long id);

    Set<Service> getServicesByEmployee(Long id);

    Service addServiceToEmployee(Long serviceId, Long employeeId);

    void deleteServiceFromEmployee(Long serviceId, Long employeeId);
}
