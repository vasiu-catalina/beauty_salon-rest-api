package com.vasiu_catalina.beauty_salon.security.exception;

import org.springframework.security.core.AuthenticationException;

public class BadCredentialsException extends AuthenticationException {

    public BadCredentialsException(String msg) {
        super(msg);
    }

    public BadCredentialsException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
