package com.vasiu_catalina.beauty_salon.security.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class MethodSecurityConfigTest {

    @Test
    void defaultConstructorExists() {
        MethodSecurityConfig config = new MethodSecurityConfig();
        assertNotNull(config);
    }
}
