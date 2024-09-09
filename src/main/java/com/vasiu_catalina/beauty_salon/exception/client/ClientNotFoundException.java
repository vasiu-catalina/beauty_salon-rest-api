package com.vasiu_catalina.beauty_salon.exception.client;

public class ClientNotFoundException extends RuntimeException {
    
    public ClientNotFoundException(Long id) {
        super("Client with ID " + id + " was not found.");
    }
}
