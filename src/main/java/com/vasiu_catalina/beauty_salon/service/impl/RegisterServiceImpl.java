package com.vasiu_catalina.beauty_salon.service.impl;

import java.io.IOException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.vasiu_catalina.beauty_salon.entity.Client;
import com.vasiu_catalina.beauty_salon.entity.Employee;
import com.vasiu_catalina.beauty_salon.security.exception.PasswordsDontMatchException;
import com.vasiu_catalina.beauty_salon.security.service.SecurityService;
import com.vasiu_catalina.beauty_salon.service.IClientService;
import com.vasiu_catalina.beauty_salon.service.IEmployeeService;
import com.vasiu_catalina.beauty_salon.service.IRegisterService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RegisterServiceImpl implements IRegisterService {

    private IClientService clientService;
    private IEmployeeService employeeService;
    private SecurityService securityService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void registerClient(Client client, HttpServletResponse response) throws ServletException, IOException {

        if (!client.getPassword().equals(client.getConfirmPassword())) {
            throw new PasswordsDontMatchException();
        }
        String encryptedPassword = bCryptPasswordEncoder.encode(client.getPassword());
        client.setPassword(encryptedPassword);

        clientService.createClient(client);
        securityService.successfulAuthentication(response, client.getEmail(), "CLIENT");

    }

    public void registerEmployee(Employee employee, HttpServletResponse response) throws ServletException, IOException {

        if (!employee.getPassword().equals(employee.getConfirmPassword())) {
            throw new PasswordsDontMatchException();
        }
        String encryptedPassword = bCryptPasswordEncoder.encode(employee.getPassword());
        employee.setPassword(encryptedPassword);

        employeeService.createEmployee(employee);
        securityService.successfulAuthentication(response, employee.getEmail(), "CLIENT");
    }
}
