package com.vasiu_catalina.beauty_salon.exception.review;

public class ReviewNotFoundException extends RuntimeException {
    
    public ReviewNotFoundException(Long clientId, Long employeeId) {
        super("Review for employee with ID of " + employeeId + " from client with ID of " + clientId + " was not found.");
    }
}
