package com.vasiu_catalina.beauty_salon.exception.service;

public class ProductNotFoundForServiceException extends RuntimeException {

    public ProductNotFoundForServiceException(String productName, String serviceName) {
        super("Product \"" + productName + "\" not found for service  \"" + serviceName + "\".");
    }
}
