package com.vasiu_catalina.beauty_salon.exception.service;

import org.springframework.dao.DataIntegrityViolationException;

public class ProductAlreadyAddedToServiceException extends DataIntegrityViolationException {

    public ProductAlreadyAddedToServiceException(String productName, String serviceName) {
        super("Product \"" + productName + "\" already added to service  \"" + serviceName + "\".");
    }
}
