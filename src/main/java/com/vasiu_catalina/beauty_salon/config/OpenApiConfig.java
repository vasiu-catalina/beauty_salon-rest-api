package com.vasiu_catalina.beauty_salon.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Beauty Salon REST API")
                        .description(
                                "An API for managing a beauty salon, including client and employee authentication (login and registration), management of services and associated products, appointment scheduling, and customer reviews.")
                        .version("v1.0")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Catalina Vasiu")
                                .url("https://github.com/vasiu-catalina")
                                .email("vasiu.catalina10@gmail.com")))
                .servers(List.of(new Server().url("http://localhost:8080/api/v1").description("Development Server")))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT Bearer Authentication - Use this to access secured endpoints.")));
    }
}
