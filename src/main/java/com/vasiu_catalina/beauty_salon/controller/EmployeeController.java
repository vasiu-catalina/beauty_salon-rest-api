package com.vasiu_catalina.beauty_salon.controller;

import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vasiu_catalina.beauty_salon.entity.Employee;
import com.vasiu_catalina.beauty_salon.entity.Service;
import com.vasiu_catalina.beauty_salon.service.IEmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    private final IEmployeeService employeeService;

    @Operation(summary = "Retrieve all employees", description = "Provides a list of all employees in the system", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all employees", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Employee.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK);
    }

    @Operation(summary = "Create a new employee", description = "Allows an authenticated employee to create a new employee", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created a new employee", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized to create a new employee")
    })
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody @Valid Employee employee) {
        return new ResponseEntity<>(employeeService.createEmployee(employee), HttpStatus.CREATED);
    }

    @Operation(summary = "Retrieve employee details", description = "Retrieve details of a specific employee by ID", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved employee details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access to employee details"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        return new ResponseEntity<>(employeeService.getEmployee(id), HttpStatus.OK);
    }

    @Operation(summary = "Update employee details", description = "Update an existing employee's details by ID", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated employee", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized to update employee"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody @Valid Employee employee) {
        return new ResponseEntity<>(employeeService.updateEmployee(id, employee), HttpStatus.OK);
    }

    @Operation(summary = "Delete an employee", description = "Allows an authenticated employee to delete an employee record by ID", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted employee"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized to delete employee"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get services by employee", description = "Retrieve all services provided by a specific employee", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved services", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Service.class)))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized to view services"),
            @ApiResponse(responseCode = "404", description = "Employee or services not found")
    })
    @GetMapping("/{id}/services")
    public ResponseEntity<Set<Service>> getServicesByEmployee(@PathVariable Long id) {
        return new ResponseEntity<>(employeeService.getServicesByEmployee(id), HttpStatus.OK);
    }

    @Operation(summary = "Add a service to an employee", description = "Allows an employee to add a service to their profile", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added service to employee", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized to add service"),
            @ApiResponse(responseCode = "404", description = "Service or employee not found")
    })
    @PutMapping("{employeeId}/services/{serviceId}")
    public ResponseEntity<Employee> addServiceToEmployee(@PathVariable Long employeeId, @PathVariable Long serviceId) {
        return new ResponseEntity<>(employeeService.addServiceToEmployee(serviceId, employeeId), HttpStatus.OK);
    }

    @Operation(summary = "Delete a service from an employee", description = "Allows an employee to remove a service from their profile", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully removed service from employee"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized to remove service"),
            @ApiResponse(responseCode = "404", description = "Service or employee not found")
    })
    @DeleteMapping("{employeeId}/services/{serviceId}")
    public ResponseEntity<HttpStatus> deleteServiceFromEmployee(@PathVariable Long employeeId,
            @PathVariable Long serviceId) {
        employeeService.deleteServiceFromEmployee(serviceId, employeeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
