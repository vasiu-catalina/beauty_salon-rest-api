package com.vasiu_catalina.beauty_salon.exception.service;

import org.springframework.dao.DataIntegrityViolationException;

public class ServiceAlreadyExistsException extends DataIntegrityViolationException {

    public ServiceAlreadyExistsException(String uniqueFieldName) {
        super("Service named \"" + uniqueFieldName + "\" already exists.");
    }
}
