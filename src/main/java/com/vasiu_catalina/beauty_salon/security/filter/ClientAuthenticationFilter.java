package com.vasiu_catalina.beauty_salon.security.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vasiu_catalina.beauty_salon.entity.Client;
import com.vasiu_catalina.beauty_salon.security.manager.ClientAuthenticationManager;
import com.vasiu_catalina.beauty_salon.security.service.SecurityService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ClientAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private ClientAuthenticationManager clientAuthenticationManager;
    private SecurityService securityService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            Client client = new ObjectMapper().readValue(request.getInputStream(), Client.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(client.getEmail(),
                    client.getPassword());

            return clientAuthenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException("Error in client authentication.");
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        securityService.unsuccessfulAuthentication(response, failed);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        securityService.successfulAuthentication(response, authResult.getName(), "CLIENT");
    }

}
