package com.vasiu_catalina.beauty_salon.security.manager;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.vasiu_catalina.beauty_salon.entity.Employee;
import com.vasiu_catalina.beauty_salon.security.exception.BadCredentialsException;
import com.vasiu_catalina.beauty_salon.service.IEmployeeService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class EmployeeAuthenticationManager implements AuthenticationManager {
    
    private IEmployeeService employeeService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
       
        Employee employee = employeeService.getEmployeeByEmail(authentication.getName());

        if (!bCryptPasswordEncoder.matches(authentication.getCredentials().toString(), employee.getPassword())) {
            throw new BadCredentialsException("Incorrect password provided!");
        }

        return new UsernamePasswordAuthenticationToken(authentication.getName(), employee.getPassword());
    }

}
