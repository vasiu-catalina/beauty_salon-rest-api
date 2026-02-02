package com.vasiu_catalina.beauty_salon;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Optional;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vasiu_catalina.beauty_salon.entity.Product;
import com.vasiu_catalina.beauty_salon.entity.Service;
import com.vasiu_catalina.beauty_salon.exception.product.ProductAlreadyExistsException;
import com.vasiu_catalina.beauty_salon.exception.product.ProductNotFoundException;
import com.vasiu_catalina.beauty_salon.repository.ProductRepository;
import com.vasiu_catalina.beauty_salon.repository.ServiceRepository;
import com.vasiu_catalina.beauty_salon.service.impl.ProductServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    public void testGetAllProducts() {

        when(productRepository.findAll()).thenReturn(List.of(createOtherProduct(), createProduct()));

        List<Product> result = productService.getAllProducts();

        assertEquals(2, result.size());
        assertEquals("Lampa manichiura", result.get(0).getName());
        assertEquals(new BigDecimal(300), result.get(1).getPrice());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testCreateProduct() {

        Product product = createProduct();
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.createProduct(product);

        assertFullResult(result);
        verify(productRepository, times(1)).save(product);
        verify(productRepository, times(1)).save(any(Product.class));

    }

    @Test
    public void testNameAlreadyTaken() {
        Product product = createProduct();
        when(productRepository.findByName(product.getName())).thenReturn(Optional.of(product));

        ProductAlreadyExistsException exception = assertThrows(ProductAlreadyExistsException.class, () -> {
            productService.createProduct(product);
        });

        assertProductAlreadyExistsException(exception, product.getName());
        verify(productRepository, times(1)).findByName(product.getName());
        verify(productRepository, never()).save(product);
    }

    @Test
    public void testGetProduct() {

        Long productId = 1L;
        Product product = createProduct();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Product result = productService.getProduct(productId);

        assertFullResult(result);
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    public void testProductNotFound() {
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.getProduct(productId);
        });

        assertProductNotFoundException(exception, productId);
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    public void testUpdateProduct() {
        Long productId = 1L;
        Product existingProduct = createOtherProduct();
        Product updatedProduct = createProduct();

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        Product result = productService.updateProduct(productId, updatedProduct);

        assertFullResult(result);
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    public void testUpdateProductNotFound() {
        Long productId = 1L;
        Product updatedProduct = createProduct();
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.updateProduct(productId, updatedProduct);
        });

        assertProductNotFoundException(exception, productId);
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void testDeleteProduct() {
        Long productId = 1L;
        Product existingProduct = createProduct();
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        productService.deleteProduct(productId);

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    public void testdeleteProductNotFound() {
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.deleteProduct(productId);
        });

        assertProductNotFoundException(exception, productId);
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).deleteById(productId);
    }

    @Test
    public void testGetServicesByProduct() {

        Long productId = 1L;
        Product product = createProduct();
        Set<Service> expectedServices = new HashSet<>(
                List.of(ServiceServiceTest.createService(), ServiceServiceTest.createOtherService()));
        product.setServices(expectedServices);
        
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Set<Service> result = productService.getServicesByProduct(productId);

        assertEquals(expectedServices, result);
        verify(productRepository, times(1)).findById(productId);

    }

    public static void assertProductNotFoundException(ProductNotFoundException exception, Long clientId) {
        ProductNotFoundException expected = new ProductNotFoundException(clientId);
        assertEquals(expected.getMessage(), exception.getMessage());
    }

    public static void assertProductAlreadyExistsException(ProductAlreadyExistsException exception,
            String uniqueFieldName) {
        ProductAlreadyExistsException expected = new ProductAlreadyExistsException(uniqueFieldName);
        assertEquals(expected.getMessage(), exception.getMessage());
    }

    public static Product createProduct() {
        return new Product("Unghii false", new BigDecimal(300), LocalDate.of(2026, 3, 15));
    }

    public static Product createOtherProduct() {
        return new Product("Lampa manichiura", new BigDecimal(200), LocalDate.of(2030, 12, 31));
    }

    private void assertFullResult(Product result) {
        assertNotNull(result);
        assertEquals("Unghii false", result.getName());
        assertEquals(LocalDate.of(2026, 3, 15), result.getExpiryDate());
        assertEquals(Integer.valueOf(0), result.getQuantity());
        assertEquals(new BigDecimal(300), result.getPrice());

    }

}
