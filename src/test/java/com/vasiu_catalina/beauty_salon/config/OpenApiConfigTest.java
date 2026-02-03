package com.vasiu_catalina.beauty_salon.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

class OpenApiConfigTest {

    @Test
    void openApiBeanHasExpectedMetadata() {
        OpenApiConfig config = new OpenApiConfig();

        OpenAPI api = config.openAPI();

        Info info = api.getInfo();
        assertEquals("Beauty Salon REST API", info.getTitle());
        assertEquals("v1.0", info.getVersion());
        assertNotNull(info.getContact());
        assertEquals("Catalina Vasiu", info.getContact().getName());

        assertNotNull(api.getServers());
        assertEquals("http://localhost:8080/api/v1", api.getServers().get(0).getUrl());

        SecurityScheme scheme = api.getComponents().getSecuritySchemes().get("bearerAuth");
        assertNotNull(scheme);
        assertEquals(SecurityScheme.Type.HTTP, scheme.getType());
        assertEquals("bearer", scheme.getScheme());
        assertEquals("JWT", scheme.getBearerFormat());
    }
}
