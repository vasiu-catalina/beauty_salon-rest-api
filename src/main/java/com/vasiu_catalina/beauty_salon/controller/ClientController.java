package com.vasiu_catalina.beauty_salon.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vasiu_catalina.beauty_salon.entity.Client;
import com.vasiu_catalina.beauty_salon.service.IClientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Clients")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private IClientService clientService;

    @Operation(summary = "Retrieve all clients", description = "Provides a list of all registered clients.", security = @SecurityRequirement(name = "bearerAuth"), responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all clients", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Client.class)))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized to access client data")
    })
    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        return new ResponseEntity<>(clientService.getAllClients(), HttpStatus.OK);
    }

    @Operation(summary = "Create a new client", description = "Allows an employee to create a new client record.", security = @SecurityRequirement(name = "bearerAuth"), responses = {
            @ApiResponse(responseCode = "201", description = "Successfully created a new client", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized to create a client")
    })
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody @Valid Client client) {
        return new ResponseEntity<>(clientService.createClient(client), HttpStatus.CREATED);
    }

    @Operation(summary = "Retrieve client details", description = "Allows an employee or the client to retrieve details of a specific client by ID.", security = @SecurityRequirement(name = "bearerAuth"), responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved client details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized to access client data"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @PreAuthorize("hasRole('EMPLOYEE') || #id == authentication.details")
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClient(@PathVariable Long id) {
        return new ResponseEntity<>(clientService.getClient(id), HttpStatus.OK);
    }

    @Operation(summary = "Update client details", description = "Allows an employee or the client to update client details.", security = @SecurityRequirement(name = "bearerAuth"), responses = {
            @ApiResponse(responseCode = "200", description = "Successfully updated client details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized to update client details"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @PreAuthorize("hasRole('EMPLOYEE') || #id == authentication.details")
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody @Valid Client client) {
        return new ResponseEntity<>(clientService.updateClient(id, client), HttpStatus.OK);
    }

    @Operation(summary = "Delete a client", description = "Allows an employee or client to delete a client record by ID.", security = @SecurityRequirement(name = "bearerAuth"), responses = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted client"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized to delete client"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @PreAuthorize("hasRole('EMPLOYEE') || #id == authentication.details")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}