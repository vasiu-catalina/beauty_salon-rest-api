package com.vasiu_catalina.beauty_salon.exception.employee;

import org.springframework.dao.DataIntegrityViolationException;

public class EmployeeAlreadyExistsException extends DataIntegrityViolationException {

    public EmployeeAlreadyExistsException(String uniqueFieldName) {
        super(uniqueFieldName + " already taken.");
    }
}
