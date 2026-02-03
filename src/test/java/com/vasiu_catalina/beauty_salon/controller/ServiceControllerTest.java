package com.vasiu_catalina.beauty_salon.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.vasiu_catalina.beauty_salon.service.impl.EmployeeServiceTest;
import com.vasiu_catalina.beauty_salon.service.impl.ProductServiceTest;
import com.vasiu_catalina.beauty_salon.service.impl.ServiceServiceTest;
import com.vasiu_catalina.beauty_salon.entity.Employee;
import com.vasiu_catalina.beauty_salon.entity.Product;
import com.vasiu_catalina.beauty_salon.entity.Service;
import com.vasiu_catalina.beauty_salon.service.IServiceService;

@ExtendWith(MockitoExtension.class)
class ServiceControllerTest {

    @Mock
    private IServiceService serviceService;

    @InjectMocks
    private ServiceController serviceController;

    @Test
    void getAllServicesReturnsOk() {
        List<Service> services = List.of(ServiceServiceTest.createService());
        when(serviceService.getAllServices()).thenReturn(services);

        ResponseEntity<List<Service>> response = serviceController.getAllServices();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(services, response.getBody());
        verify(serviceService).getAllServices();
    }

    @Test
    void createServiceReturnsCreated() {
        Service service = ServiceServiceTest.createService();
        when(serviceService.createService(service)).thenReturn(service);

        ResponseEntity<Service> response = serviceController.createService(service);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(service, response.getBody());
        verify(serviceService).createService(service);
    }

    @Test
    void getServiceReturnsOk() {
        Service service = ServiceServiceTest.createService();
        when(serviceService.getService(1L)).thenReturn(service);

        ResponseEntity<Service> response = serviceController.getService(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(service, response.getBody());
        verify(serviceService).getService(1L);
    }

    @Test
    void updateServiceReturnsOk() {
        Service service = ServiceServiceTest.createService();
        when(serviceService.updateService(1L, service)).thenReturn(service);

        ResponseEntity<Service> response = serviceController.updateService(1L, service);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(service, response.getBody());
        verify(serviceService).updateService(1L, service);
    }

    @Test
    void deleteServiceReturnsNoContent() {
        ResponseEntity<HttpStatus> response = serviceController.deleteService(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(serviceService).deleteService(1L);
    }

    @Test
    void getEmployeesByServiceReturnsOk() {
        Set<Employee> employees = Set.of(EmployeeServiceTest.createEmployee());
        when(serviceService.getEmployeesByService(1L)).thenReturn(employees);

        ResponseEntity<Set<Employee>> response = serviceController.getEmployeesByService(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(employees, response.getBody());
        verify(serviceService).getEmployeesByService(1L);
    }

    @Test
    void getProductsByServiceReturnsOk() {
        Set<Product> products = Set.of(ProductServiceTest.createProduct());
        when(serviceService.getProductsByService(1L)).thenReturn(products);

        ResponseEntity<Set<Product>> response = serviceController.getProductsByService(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(products, response.getBody());
        verify(serviceService).getProductsByService(1L);
    }

    @Test
    void addProductToServiceReturnsOk() {
        Product product = ProductServiceTest.createProduct();
        when(serviceService.addProductToService(2L, 1L)).thenReturn(product);

        ResponseEntity<Product> response = serviceController.addProductToService(1L, 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(product, response.getBody());
        verify(serviceService).addProductToService(2L, 1L);
    }

    @Test
    void deleteProductFromServiceReturnsNoContent() {
        ResponseEntity<HttpStatus> response = serviceController.deleteProductFromService(1L, 2L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(serviceService).deleteProductFromService(2L, 1L);
    }
}
