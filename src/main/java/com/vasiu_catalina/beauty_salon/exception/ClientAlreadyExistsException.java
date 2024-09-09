package com.vasiu_catalina.beauty_salon.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class ClientAlreadyExistsException extends DataIntegrityViolationException {

    public ClientAlreadyExistsException(String uniqueFieldName) {
        super(uniqueFieldName + " already taken.");
    }
}
