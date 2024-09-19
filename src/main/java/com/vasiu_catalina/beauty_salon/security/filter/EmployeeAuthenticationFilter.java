package com.vasiu_catalina.beauty_salon.security.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.vasiu_catalina.beauty_salon.entity.Employee;
import com.vasiu_catalina.beauty_salon.security.manager.EmployeeAuthenticationManager;
import com.vasiu_catalina.beauty_salon.security.service.SecurityService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmployeeAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private EmployeeAuthenticationManager authenticationManager;
    private SecurityService securityService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            Employee employee = new ObjectMapper().readValue(request.getInputStream(), Employee.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(employee.getEmail(),
                    employee.getPassword());

            return authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException("Error in employee authentication.");
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
        securityService.successfulAuthentication(response, authResult.getName(), "EMPLOYEE");
    }

}
