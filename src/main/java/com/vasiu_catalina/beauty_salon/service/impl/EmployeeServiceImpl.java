package com.vasiu_catalina.beauty_salon.service.impl;

import java.util.Set;
import java.util.List;
import java.util.Optional;

import com.vasiu_catalina.beauty_salon.entity.Employee;
import com.vasiu_catalina.beauty_salon.entity.Service;
import com.vasiu_catalina.beauty_salon.exception.employee.EmployeeAlreadyExistsException;
import com.vasiu_catalina.beauty_salon.exception.employee.EmployeeNotFoundException;
import com.vasiu_catalina.beauty_salon.repository.EmployeeRepository;
import com.vasiu_catalina.beauty_salon.repository.ServiceRepository;
import com.vasiu_catalina.beauty_salon.service.IEmployeeService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@org.springframework.stereotype.Service
public class EmployeeServiceImpl implements IEmployeeService {

    private EmployeeRepository employeeRepository;
    private ServiceRepository serviceRepository;

    @Override
    public List<Employee> getAllEmployees() {
        return (List<Employee>) employeeRepository.findAll();
    }

    @Override
    public Employee getEmployee(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return unwrappedEmployee(employee, id);
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

        if (!existingemployee.getPhoneNumber().equals(employee.getPhoneNumber())) {

            if (existsEmployeeByPhoneNumber(employee.getPhoneNumber()))
                throw new EmployeeAlreadyExistsException("Phone number");

            existingemployee.setPhoneNumber(employee.getPhoneNumber());
        }

        existingemployee.setFirstName(employee.getFirstName());
        existingemployee.setLastName(employee.getLastName());
        existingemployee.setBirthDate(employee.getBirthDate());
        existingemployee.setAddress(employee.getAddress());
        existingemployee.setRole(employee.getRole());
        existingemployee.setSalary(employee.getSalary());
        existingemployee.setSpecialization(employee.getSpecialization());

        return employeeRepository.save(existingemployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        this.getEmployee(id);
        employeeRepository.deleteById(id);
    }

    @Override
    public Set<Service> getServicesByEmployee(Long id) {
        Employee employee = this.getEmployee(id);
        return employee.getServices();
    }

    @Override
    public Employee addServiceToEmployee(Long serviceId, Long employeeId) {

        Employee employee = this.getEmployee(employeeId);
        Service service = this.getService(serviceId);

        employee.getServices().add(service);
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteServiceFromEmployee(Long serviceId, Long employeeId) {

        Employee employee = this.getEmployee(employeeId);
        Service service = this.getService(serviceId);

        employee.getServices().remove(service);
        employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployeeByEmail(String email) {
        Optional<Employee> existingEmployeeByEmail = employeeRepository.findByEmail(email);
        return unwrappedEmployeeWithPassword(existingEmployeeByEmail, null);
    }

    private boolean existsEmployeeByEmail(String email) {
        Optional<Employee> existingEmployeeByEmail = employeeRepository.findByEmail(email);
        return existingEmployeeByEmail.isPresent();
    }

    private boolean existsEmployeeByPhoneNumber(String phoneNumber) {
        Optional<Employee> existingEmployeeByPhoneNumber = employeeRepository.findByPhoneNumber(phoneNumber);
        return existingEmployeeByPhoneNumber.isPresent();
    }

    private Service getService(Long serviceId) {
        Optional<Service> service = serviceRepository.findById(serviceId);
        return ServiceServiceImpl.unwrappedService(service, serviceId);
    }

    static Employee unwrappedEmployee(Optional<Employee> employee, Long id) {
        if (employee.isPresent()) {
            Employee existing = employee.get();
            existing.setPassword(null);
            return existing;
        }
        throw new EmployeeNotFoundException(id);
    }

    static Employee unwrappedEmployeeWithPassword(Optional<Employee> employee, Long id) {
        if (employee.isPresent())
            return employee.get();
        throw new EmployeeNotFoundException(id);
    }

}
