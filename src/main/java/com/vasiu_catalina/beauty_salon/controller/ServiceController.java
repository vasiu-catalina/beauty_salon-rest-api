package com.vasiu_catalina.beauty_salon.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vasiu_catalina.beauty_salon.entity.Employee;
import com.vasiu_catalina.beauty_salon.entity.Product;
import com.vasiu_catalina.beauty_salon.entity.Service;
import com.vasiu_catalina.beauty_salon.service.IServiceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Services")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/services")
public class ServiceController {

    private final IServiceService serviceService;

    @Operation(summary = "Get all services", description = "Retrieves a list of all services.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all services", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<Service>> getAllServices() {
        return new ResponseEntity<>(serviceService.getAllServices(), HttpStatus.OK);
    }

    @Operation(summary = "Create a service", description = "Creates a new service.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created service", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized to create service")
    })
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping
    public ResponseEntity<Service> createService(@RequestBody @Valid Service service) {
        return new ResponseEntity<>(serviceService.createService(service), HttpStatus.CREATED);
    }

    @Operation(summary = "Get a service by ID", description = "Retrieves a specific service by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved service", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Service not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Service> getService(@PathVariable Long id) {
        return new ResponseEntity<>(serviceService.getService(id), HttpStatus.OK);
    }

    @Operation(summary = "Update a service", description = "Updates an existing service by its ID.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated service", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized to update service"),
            @ApiResponse(responseCode = "404", description = "Service not found")
    })
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PutMapping("/{id}")
    public ResponseEntity<Service> updateService(@PathVariable Long id, @RequestBody @Valid Service service) {
        return new ResponseEntity<>(serviceService.updateService(id, service), HttpStatus.OK);
    }

    @Operation(summary = "Delete a service", description = "Deletes a service by its ID.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted service"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized to delete service"),
            @ApiResponse(responseCode = "404", description = "Service not found")
    })
    @PreAuthorize("hasRole('EMPLOYEE')")
    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get employees by service", description = "Retrieves a set of employees associated with a specific service.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved employees for service", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Service not found")
    })
    @GetMapping("{serviceId}/employees")
    public ResponseEntity<Set<Employee>> getEmployeesByService(@PathVariable Long serviceId) {
        return new ResponseEntity<>(serviceService.getEmployeesByService(serviceId), HttpStatus.OK);
    }

    @Operation(summary = "Get products by service", description = "Retrieves a set of products associated with a specific service.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved products for service", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized to view products"),
            @ApiResponse(responseCode = "404", description = "Service not found")
    })
    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("{serviceId}/products")
    public ResponseEntity<Set<Product>> getProductsByService(@PathVariable Long serviceId) {
        return new ResponseEntity<>(serviceService.getProductsByService(serviceId), HttpStatus.OK);
    }

    @Operation(summary = "Add a product to a service", description = "Associates a product with a specific service.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added product to service", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized to add product"),
            @ApiResponse(responseCode = "404", description = "Service or product not found")
    })
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PutMapping("/{serviceId}/products/{productId}")
    public ResponseEntity<Product> addProductToService(@PathVariable Long serviceId, @PathVariable Long productId) {
        return new ResponseEntity<>(serviceService.addProductToService(productId, serviceId), HttpStatus.OK);
    }

    @Operation(summary = "Remove a product from a service", description = "Removes an associated product from a specific service.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully removed product from service"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized to remove product"),
            @ApiResponse(responseCode = "404", description = "Service or product not found")
    })
    @PreAuthorize("hasRole('EMPLOYEE')")
    @DeleteMapping("/{serviceId}/products/{productId}")
    public ResponseEntity<HttpStatus> deleteProductFromService(@PathVariable Long serviceId,
            @PathVariable Long productId) {
        serviceService.deleteProductFromService(productId, serviceId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
