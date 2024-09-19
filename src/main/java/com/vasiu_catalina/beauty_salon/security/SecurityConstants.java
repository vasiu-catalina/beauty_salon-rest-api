package com.vasiu_catalina.beauty_salon.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "security")
public class SecurityConstants {

    private Jwt jwt;
    private Cookie cookie;
    private Path path;

    @Getter
    @Setter
    public static class Jwt {

        private String authorization;
        private String bearer;
        private String secret;
        private int token_expiration_days;
    }

    @Getter
    @Setter
    public static class Path {
        private String client_register;
        private String client_login;
        private String employee_register;
        private String employee_login;
        private String logout;
    }

    @Getter
    @Setter
    public static class Cookie {
        private String name;
        private boolean httponly;
        private boolean secure;
        private String path;
        private int expiration_days;
    }
}
