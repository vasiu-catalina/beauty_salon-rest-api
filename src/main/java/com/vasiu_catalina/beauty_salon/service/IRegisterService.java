package com.vasiu_catalina.beauty_salon.service;

import java.io.IOException;

import com.vasiu_catalina.beauty_salon.entity.Client;
import com.vasiu_catalina.beauty_salon.entity.Employee;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

public interface IRegisterService {

    public void registerClient(Client client, HttpServletResponse response) throws ServletException, IOException;

    public void registerEmployee(Employee employee, HttpServletResponse response) throws ServletException, IOException;
}
