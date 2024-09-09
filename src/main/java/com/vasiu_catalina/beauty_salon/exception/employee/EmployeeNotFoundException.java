package com.vasiu_catalina.beauty_salon.exception.employee;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(Long id) {
        super("Employee with ID " + id + " was not found.");
    }
}
