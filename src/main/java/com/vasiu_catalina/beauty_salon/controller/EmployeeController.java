package com.vasiu_catalina.beauty_salon.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vasiu_catalina.beauty_salon.entity.Employee;
import com.vasiu_catalina.beauty_salon.entity.Service;
import com.vasiu_catalina.beauty_salon.service.IEmployeeService;
import com.vasiu_catalina.beauty_salon.service.IRegisterService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Employees")
@AllArgsConstructor
@RestController
@PreAuthorize("hasRole('EMPLOYEE')")
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private IEmployeeService employeeService;
    private IRegisterService registerService;

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody @Valid Employee employee) {
        return new ResponseEntity<>(employeeService.createEmployee(employee), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        return new ResponseEntity<>(employeeService.getEmployee(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody @Valid Employee employee) {
        return new ResponseEntity<>(employeeService.updateEmployee(id, employee), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/services")
    public ResponseEntity<Set<Service>> getServicesByEmployee(@PathVariable Long id) {
        return new ResponseEntity<>(employeeService.getServicesByEmployee(id), HttpStatus.OK);
    }

    @PutMapping("{employeeId}/services/{serviceId}")
    public ResponseEntity<Employee> addServiceToEmployee(@PathVariable Long employeeId, @PathVariable Long serviceId) {
        return new ResponseEntity<>(employeeService.addServiceToEmployee(serviceId, employeeId), HttpStatus.OK);
    }

    @DeleteMapping("{employeeId}/services/{serviceId}")
    public ResponseEntity<HttpStatus> deleteServiceFromEmployee(@PathVariable Long employeeId,
            @PathVariable Long serviceId) {
        employeeService.deleteServiceFromEmployee(serviceId, employeeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> registerEmployee(@RequestBody @Valid Employee employee,
            HttpServletResponse response) throws ServletException, IOException {
        registerService.registerEmployee(employee, response);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
