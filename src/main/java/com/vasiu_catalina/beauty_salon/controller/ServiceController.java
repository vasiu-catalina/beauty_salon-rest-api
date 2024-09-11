package com.vasiu_catalina.beauty_salon.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vasiu_catalina.beauty_salon.entity.Employee;
import com.vasiu_catalina.beauty_salon.entity.Product;
import com.vasiu_catalina.beauty_salon.entity.Service;
import com.vasiu_catalina.beauty_salon.service.ServiceService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@AllArgsConstructor
@RestController
@RequestMapping("/services")
public class ServiceController {

    private ServiceService serviceService;

    @GetMapping
    public ResponseEntity<List<Service>> getAllServices() {
        return new ResponseEntity<>(serviceService.getAllServices(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Service> createService(@RequestBody @Valid Service service) {
        return new ResponseEntity<>(serviceService.createService(service), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Service> getService(@PathVariable Long id) {
        return new ResponseEntity<>(serviceService.getService(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Service> updateService(@PathVariable Long id, @RequestBody @Valid Service service) {
        return new ResponseEntity<>(serviceService.updateService(id, service), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("{serviceId}/employees")
    public ResponseEntity<Set<Employee>> getEmployeesByService(@PathVariable Long serviceId) {
        return new ResponseEntity<>(serviceService.getEmployeesByService(serviceId), HttpStatus.OK);
    }

    @GetMapping("{serviceId}/products")
    public ResponseEntity<Set<Product>> getProductsByService(@PathVariable Long serviceId) {
        return new ResponseEntity<>(serviceService.getProductsByService(serviceId), HttpStatus.OK);
    }

    @PutMapping("/{serviceId}/products/{productId}")
    public ResponseEntity<Service> addProductToService(@PathVariable Long serviceId, @PathVariable Long productId) {
        return new ResponseEntity<>(serviceService.addProductToService(productId, serviceId), HttpStatus.OK);
    }

    public ResponseEntity<HttpStatus> deleteProductFromService(@PathVariable Long serviceId, @PathVariable Long productId) {
        serviceService.deleteProductFromService(productId, serviceId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
