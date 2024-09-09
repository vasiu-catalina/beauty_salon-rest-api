package com.vasiu_catalina.beauty_salon.exception.service;

public class ServiceNotFoundException extends RuntimeException {
    public ServiceNotFoundException(Long id) {
        super("Service with ID " + id + " was not found.");
    }
}
