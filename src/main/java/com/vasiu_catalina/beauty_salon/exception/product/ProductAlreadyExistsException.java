package com.vasiu_catalina.beauty_salon.exception.product;

import org.springframework.dao.DataIntegrityViolationException;

public class ProductAlreadyExistsException extends DataIntegrityViolationException {

    public ProductAlreadyExistsException(String uniqueFieldName) {
        super("Product named \"" + uniqueFieldName + "\" already exists.");
    }
}
