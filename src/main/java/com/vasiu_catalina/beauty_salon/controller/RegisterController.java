package com.vasiu_catalina.beauty_salon.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vasiu_catalina.beauty_salon.entity.Client;
import com.vasiu_catalina.beauty_salon.entity.Employee;
import com.vasiu_catalina.beauty_salon.service.IRegisterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Tag(name = "Registration")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class RegisterController {

    private IRegisterService registerService;

    @Operation(summary = "Register a new client", description = "Allows a new client to register in the system without authentication.", responses = {
            @ApiResponse(responseCode = "201", description = "Successfully registered client", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/clients/register")
    public ResponseEntity<HttpStatus> registerClient(
            @RequestBody @Valid Client client,
            HttpServletResponse response) throws ServletException, IOException {
        registerService.registerClient(client, response);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Register a new employee", description = "Allows new employees to register themselves without authentication.", responses = {
            @ApiResponse(responseCode = "201", description = "Successfully registered new employee", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PreAuthorize("permitAll()")
    @PostMapping("/employees/register")
    public ResponseEntity<HttpStatus> registerEmployee(
            @RequestBody @Valid Employee employee,
            HttpServletResponse response) throws ServletException, IOException {
        registerService.registerEmployee(employee, response);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
