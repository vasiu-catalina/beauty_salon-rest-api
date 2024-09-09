package com.vasiu_catalina.beauty_salon.service.impl;

import java.util.List;
import java.util.Optional;

import com.vasiu_catalina.beauty_salon.entity.Employee;
import com.vasiu_catalina.beauty_salon.entity.Service;
import com.vasiu_catalina.beauty_salon.exception.employee.EmployeeAlreadyExistsException;
import com.vasiu_catalina.beauty_salon.exception.employee.EmployeeNotFoundException;
import com.vasiu_catalina.beauty_salon.exception.service.ServiceNotFoundException;
import com.vasiu_catalina.beauty_salon.repository.EmployeeRepository;
import com.vasiu_catalina.beauty_salon.repository.ServiceRepository;
import com.vasiu_catalina.beauty_salon.service.EmployeeService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@org.springframework.stereotype.Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    private ServiceRepository serviceRepository;


    @Override
    public List<Employee> getAllEmployees() {
        return (List<Employee>) employeeRepository.findAll();
    }

    @Override
    public Employee getEmployee(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @Override
    public Employee createEmployee(Employee employee) {
        if (existsEmployeeByEmail(employee.getEmail()))
            throw new EmployeeAlreadyExistsException("Email");
        if (existsEmployeeByPhoneNumber(employee.getPhoneNumber()))
            throw new EmployeeAlreadyExistsException("Phone number");
        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(Long id, Employee employee) {
        Employee existingemployee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        if (!existingemployee.getEmail().equals(employee.getEmail())) {
            if (existsEmployeeByEmail(employee.getEmail()))
                throw new EmployeeAlreadyExistsException("Email");
            existingemployee.setEmail(employee.getEmail());
        }
        if (!existingemployee.getEmail().equals(employee.getEmail())) {
            if (existsEmployeeByPhoneNumber(employee.getPhoneNumber()))
                throw new EmployeeAlreadyExistsException("Phone number");
            existingemployee.setPhoneNumber(employee.getPhoneNumber());
        }
        existingemployee.setFirstName(employee.getFirstName());
        existingemployee.setLastName(employee.getLastName());
        existingemployee.setBirthDate(employee.getBirthDate());
        existingemployee.setAddress(employee.getAddress());
        existingemployee.setRole(employee.getRole());
        existingemployee.setSpecialization(employee.getSpecialization());
        existingemployee.setSalary(employee.getSalary());

        return employeeRepository.save(existingemployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public List<Service> getServicesByEmployee(Long id) {
        return (List<Service>) serviceRepository.findAllByEmployeeId(id);
    }

    @Override
    public Service addServiceToEmployee(Long serviceId, Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        Service service = serviceRepository.findById(serviceId).orElseThrow(() -> new ServiceNotFoundException(serviceId));
        employee.getServices().add(service);
        employeeRepository.save(employee);
        return service;
    }

    @Override
    public void deleteServiceFromEmployee(Long serviceId, Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        Service service = serviceRepository.findById(serviceId).orElseThrow(() -> new ServiceNotFoundException(serviceId));
        employee.getServices().remove(service);
        employeeRepository.save(employee);
    }

    private boolean existsEmployeeByEmail(String email) {
        Optional<Employee> existingEmployeeByEmail = employeeRepository.findByEmail(email);
        return existingEmployeeByEmail.isPresent();
    }

    private boolean existsEmployeeByPhoneNumber(String phoneNumber) {
        Optional<Employee> existingEmployeeByPhoneNumber = employeeRepository.findByPhoneNumber(phoneNumber);
        return existingEmployeeByPhoneNumber.isPresent();
    }
}
