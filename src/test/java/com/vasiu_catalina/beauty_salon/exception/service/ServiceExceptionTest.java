package com.vasiu_catalina.beauty_salon.exception.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ServiceExceptionTest {

    @Test
    void serviceAlreadyExistsMessageIsFormatted() {
        ServiceAlreadyExistsException ex = new ServiceAlreadyExistsException("Manicure");
        assertEquals("Service named \"Manicure\" already exists.", ex.getMessage());
    }

    @Test
    void serviceNotFoundMessageIsFormatted() {
        ServiceNotFoundException ex = new ServiceNotFoundException(5L);
        assertEquals("Service with ID 5 was not found.", ex.getMessage());
    }

    @Test
    void productNotFoundForServiceMessageIsFormatted() {
        ProductNotFoundForServiceException ex = new ProductNotFoundForServiceException("Oil", "Spa");
        assertEquals("Product \"Oil\" not found for service  \"Spa\".", ex.getMessage());
    }

    @Test
    void productAlreadyAddedMessageIsFormatted() {
        ProductAlreadyAddedToServiceException ex = new ProductAlreadyAddedToServiceException("Oil", "Spa");
        assertEquals("Product \"Oil\" already added to service  \"Spa\".", ex.getMessage());
    }
}
