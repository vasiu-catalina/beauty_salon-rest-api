package com.vasiu_catalina.beauty_salon.security.exception;

import org.springframework.security.core.AuthenticationException;

public class ForbiddenException extends AuthenticationException {

    public ForbiddenException(String msg) {
        super(msg);
    }

    public ForbiddenException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
}
