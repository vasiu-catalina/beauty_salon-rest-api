package com.vasiu_catalina.beauty_salon.service;

import java.util.Set;

import com.vasiu_catalina.beauty_salon.entity.Product;
import com.vasiu_catalina.beauty_salon.entity.Service;

public interface IProductService {
    
    Set<Product> getAllProducts();

    Product createProduct(Product product);

    Product getProduct(Long id);

    Product updateProduct(Long id, Product product);

    void deleteProduct(Long id);

    Set<Service> getServicesByProduct(Long id);
}
