package com.vasiu_catalina.beauty_salon;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Optional;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.vasiu_catalina.beauty_salon.entity.Product;
import com.vasiu_catalina.beauty_salon.entity.Service;
import com.vasiu_catalina.beauty_salon.exception.product.ProductAlreadyExistsException;
import com.vasiu_catalina.beauty_salon.exception.product.ProductNotFoundException;
import com.vasiu_catalina.beauty_salon.repository.ProductRepository;
import com.vasiu_catalina.beauty_salon.repository.ServiceRepository;
import com.vasiu_catalina.beauty_salon.service.impl.ProductServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    public void testGetAllProducts() {

        when(productRepository.findAll()).thenReturn(List.of(
                new Product("Lampa manichiura", new BigDecimal(200), LocalDate.of(2030, 12, 31)),
                new Product("Unghii false", new BigDecimal(300), LocalDate.of(2026, 3, 15))));

        List<Product> result = productService.getAllProducts();

        assertEquals(2, result.size());
        assertEquals("Lampa manichiura", result.get(0).getName());
        assertEquals(new BigDecimal(300), result.get(1).getPrice());
    }

    @Test
    public void testCreateProduct() {
        // Product clinet =

        Product product = new Product("Unghii false", new BigDecimal(300), LocalDate.of(2026, 3, 15));

        when(productRepository.save(any(Product.class)))
                .thenReturn(product)
                .thenReturn(product);

        Product result = productService.createProduct(product);

        verify(productRepository, times(1)).save(product);
        assertNotNull(result);

        assertEquals("Unghii false", result.getName());
        assertEquals(LocalDate.of(2026, 3, 15), result.getExpiryDate());
        assertEquals(Integer.valueOf(0), result.getQuantity());
        assertEquals(new BigDecimal(300), result.getPrice());

        verify(productRepository, times(1)).save(any(Product.class));

    }

    @Test
    public void testNameAlreadyTaken() {
        Product product = new Product("Unghii false", new BigDecimal(300), LocalDate.of(2026, 3, 15));

        when(productRepository.findByName(product.getName())).thenReturn(Optional.of(product));

        ProductAlreadyExistsException exception = assertThrows(ProductAlreadyExistsException.class, () -> {
            productService.createProduct(product);
        });

        assertEquals("Product named \"" + product.getName() + "\" already exists.", exception.getMessage());

        verify(productRepository, times(1)).findByName(product.getName());
        verify(productRepository, never()).save(product);
    }


    @Test
    public void testGetProduct() {

        Long productId = 1L;
        Product product = new Product("Unghii false", new BigDecimal(300), LocalDate.of(2026, 3, 15));

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Product result = productService.getProduct(productId);

        assertNotNull(result);
        assertEquals("Unghii false", result.getName());
        assertEquals(LocalDate.of(2026, 3, 15), result.getExpiryDate());
        assertEquals(Integer.valueOf(0), result.getQuantity());
        assertEquals(new BigDecimal(300), result.getPrice());

        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    public void testProductNotFound() {
        Long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.getProduct(productId);
        });

        assertEquals("Product with ID of " + productId + " was not found.", exception.getMessage());

        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    public void testUpdateProduct() {
        Long productId = 1L;
        Product existingProduct = new Product("Unghii false", new BigDecimal(300), LocalDate.of(2026, 3, 15));
        Product updatedProduct = new Product("Unghii false", new BigDecimal(300), LocalDate.of(2026, 3, 15));

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        Product result = productService.updateProduct(productId, updatedProduct);

        assertNotNull(result);
        assertEquals("Unghii false", result.getName());
        assertEquals(LocalDate.of(2026, 3, 15), result.getExpiryDate());
        assertEquals(Integer.valueOf(0), result.getQuantity());
        assertEquals(new BigDecimal(300), result.getPrice());

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    public void testUpdateProductNotFound() {
        Long productId = 1L;
        Product updatedProduct = new Product("Unghii false", new BigDecimal(300), LocalDate.of(2026, 3, 15));

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.updateProduct(productId, updatedProduct);
        });

        assertEquals("Product with ID of " + productId + " was not found.", exception.getMessage());

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void testDeleteProduct() {
        // arrange
        Long productId = 1L;
        Product existingProduct = new Product("Unghii false", new BigDecimal(300), LocalDate.of(2026, 3, 15));

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        // act
        productService.deleteProduct(productId);

        // assert
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

        assertEquals("Product with ID of " + productId + " was not found.", exception.getMessage());

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).deleteById(productId);
    }

    @Test
    public void testGetServicesByProduct() {

        Long productId = 1L;
        Product product = new Product("Unghii false", new BigDecimal(300), LocalDate.of(2026, 3, 15));

        Set<Service> expectedServices = new HashSet<>(List.of(
                new Service("Manichiura", "Serviciu bun", new BigDecimal(100), 60),
                new Service("Renovare unghii", "Serviciu excelent", new BigDecimal(140), 80)));

        product.setServices(expectedServices);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Set<Service> result = productService.getServicesByProduct(productId);

        assertEquals(expectedServices, result);
        verify(productRepository, times(1)).findById(productId);

    }

}
