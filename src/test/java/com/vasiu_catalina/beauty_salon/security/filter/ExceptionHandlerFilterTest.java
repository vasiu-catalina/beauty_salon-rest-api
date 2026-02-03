package com.vasiu_catalina.beauty_salon.security.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.vasiu_catalina.beauty_salon.exception.client.ClientNotFoundException;

import jakarta.servlet.FilterChain;

class ExceptionHandlerFilterTest {

    @Test
    void handlesClientNotFoundException() throws Exception {
        ExceptionHandlerFilter filter = new ExceptionHandlerFilter();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = (req, res) -> {
            throw new ClientNotFoundException(1L);
        };

        filter.doFilterInternal(request, response, chain);

        assertEquals(404, response.getStatus());
        assertEquals("Client does not exist", response.getContentAsString());
    }

    @Test
    void handlesJwtVerificationException() throws Exception {
        ExceptionHandlerFilter filter = new ExceptionHandlerFilter();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = (req, res) -> {
            throw new JWTVerificationException("Invalid token");
        };

        filter.doFilterInternal(request, response, chain);

        assertEquals(403, response.getStatus());
        assertEquals("Invalid token", response.getContentAsString());
    }

    @Test
    void handlesRuntimeException() throws Exception {
        ExceptionHandlerFilter filter = new ExceptionHandlerFilter();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = (req, res) -> {
            throw new RuntimeException("Boom");
        };

        filter.doFilterInternal(request, response, chain);

        assertEquals(400, response.getStatus());
        assertEquals("Boom", response.getContentAsString());
    }
}
