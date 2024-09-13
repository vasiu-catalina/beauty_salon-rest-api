package com.vasiu_catalina.beauty_salon.exception.review;

import org.springframework.dao.DataIntegrityViolationException;

public class ReviewAlreadyExistsException extends DataIntegrityViolationException {

    public ReviewAlreadyExistsException(Long clientId, Long employeeId) {
        super("Review for employee with ID of " + employeeId + " from client with ID of " + clientId + " already exists.");
    }
}
