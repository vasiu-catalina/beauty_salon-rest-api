package com.vasiu_catalina.beauty_salon.security.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class SecurityExceptionTest {

    @Test
    void badCredentialsExceptionStoresMessage() {
        BadCredentialsException ex = new BadCredentialsException("Invalid credentials");
        assertEquals("Invalid credentials", ex.getMessage());
    }

    @Test
    void badCredentialsExceptionStoresCause() {
        RuntimeException cause = new RuntimeException("Cause");
        BadCredentialsException ex = new BadCredentialsException("Invalid", cause);
        assertSame(cause, ex.getCause());
        assertEquals("Invalid", ex.getMessage());
    }

    @Test
    void forbiddenExceptionStoresMessage() {
        ForbiddenException ex = new ForbiddenException("Forbidden");
        assertEquals("Forbidden", ex.getMessage());
    }

    @Test
    void forbiddenExceptionStoresCause() {
        RuntimeException cause = new RuntimeException("Cause");
        ForbiddenException ex = new ForbiddenException("Forbidden", cause);
        assertSame(cause, ex.getCause());
    }

    @Test
    void passwordsDontMatchHasDefaultMessage() {
        PasswordsDontMatchException ex = new PasswordsDontMatchException();
        assertEquals("Passwords don't match.", ex.getMessage());
    }
}
