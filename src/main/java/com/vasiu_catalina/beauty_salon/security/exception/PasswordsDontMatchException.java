package com.vasiu_catalina.beauty_salon.security.exception;

public class PasswordsDontMatchException extends RuntimeException {
    
    public PasswordsDontMatchException() {
        super("Passwords don't match.");
    }
}
