package com.vasiu_catalina.beauty_salon.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

class HealthControllerTest {

    @Test
    void healthReturnsStatusAndTimestamp() {
        HealthController controller = new HealthController();

        ResponseEntity<Map<String, Object>> response = controller.health();

        assertEquals(200, response.getStatusCode().value());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("UP", body.get("status"));
        assertNotNull(body.get("timestamp"));
    }
}
