package com.vasiu_catalina.beauty_salon.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vasiu_catalina.beauty_salon.entity.Product;
import com.vasiu_catalina.beauty_salon.entity.Service;
import com.vasiu_catalina.beauty_salon.service.IProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Tag(name = "Products")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final IProductService productService;

    @Operation(summary = "Get all products", description = "Retrieves a list of all products.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of products", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Product.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Create a new product", description = "Creates a new product in the system.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Invalid product data"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody @Valid Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @Operation(summary = "Get a product by ID", description = "Retrieves a specific product by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        Product product = productService.getProduct(id);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Update a product", description = "Updates an existing product by its ID.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Invalid product data"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody @Valid Product product) {
        Product updatedProduct = productService.updateProduct(id, product);
        return ResponseEntity.ok(updatedProduct);
    }

    @Operation(summary = "Delete a product", description = "Deletes a product by its ID.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("hasRole('EMPLOYEE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get services by product", description = "Retrieves a set of services associated with a specific product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Services found for product", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Service.class)))),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{productId}/services")
    public ResponseEntity<Set<Service>> getServicesByProduct(@PathVariable Long productId) {
        Set<Service> services = productService.getServicesByProduct(productId);
        return ResponseEntity.ok(services);
    }
}
