package com.vasiu_catalina.beauty_salon.exception.product;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Product with ID " + id + " was not found.");
    }
}
