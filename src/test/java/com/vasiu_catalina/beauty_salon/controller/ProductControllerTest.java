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

import com.vasiu_catalina.beauty_salon.service.impl.ProductServiceTest;
import com.vasiu_catalina.beauty_salon.service.impl.ServiceServiceTest;
import com.vasiu_catalina.beauty_salon.entity.Product;
import com.vasiu_catalina.beauty_salon.entity.Service;
import com.vasiu_catalina.beauty_salon.service.IProductService;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private IProductService productService;

    @InjectMocks
    private ProductController productController;

    @Test
    void getAllProductsReturnsOk() {
        List<Product> products = List.of(ProductServiceTest.createProduct(), ProductServiceTest.createOtherProduct());
        when(productService.getAllProducts()).thenReturn(products);

        ResponseEntity<List<Product>> response = productController.getAllProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(products, response.getBody());
        verify(productService).getAllProducts();
    }

    @Test
    void createProductReturnsCreated() {
        Product product = ProductServiceTest.createProduct();
        when(productService.createProduct(product)).thenReturn(product);

        ResponseEntity<Product> response = productController.createProduct(product);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(product, response.getBody());
        verify(productService).createProduct(product);
    }

    @Test
    void getProductReturnsOk() {
        Product product = ProductServiceTest.createProduct();
        when(productService.getProduct(1L)).thenReturn(product);

        ResponseEntity<Product> response = productController.getProduct(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(product, response.getBody());
        verify(productService).getProduct(1L);
    }

    @Test
    void updateProductReturnsOk() {
        Product product = ProductServiceTest.createProduct();
        when(productService.updateProduct(1L, product)).thenReturn(product);

        ResponseEntity<Product> response = productController.updateProduct(1L, product);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(product, response.getBody());
        verify(productService).updateProduct(1L, product);
    }

    @Test
    void deleteProductReturnsNoContent() {
        ResponseEntity<HttpStatus> response = productController.deleteProduct(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productService).deleteProduct(1L);
    }

    @Test
    void getServicesByProductReturnsOk() {
        Set<Service> services = Set.of(ServiceServiceTest.createService());
        when(productService.getServicesByProduct(1L)).thenReturn(services);

        ResponseEntity<Set<Service>> response = productController.getServicesByProduct(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(services, response.getBody());
        verify(productService).getServicesByProduct(1L);
    }
}
