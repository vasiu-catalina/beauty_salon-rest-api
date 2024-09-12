package com.vasiu_catalina.beauty_salon.service.impl;

import java.util.List;
import java.util.Set;
import java.util.Optional;

import com.vasiu_catalina.beauty_salon.entity.Product;
import com.vasiu_catalina.beauty_salon.entity.Service;
import com.vasiu_catalina.beauty_salon.exception.product.ProductAlreadyExistsException;
import com.vasiu_catalina.beauty_salon.exception.product.ProductNotFoundException;
import com.vasiu_catalina.beauty_salon.repository.ProductRepository;
import com.vasiu_catalina.beauty_salon.service.IProductService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@org.springframework.stereotype.Service
public class ProductServiceImpl implements IProductService {

    private ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return (List<Product>) productRepository.findAll();
    }

    @Override
    public Product createProduct(Product product) {

        if (existsProductByName(product.getName()))
            throw new ProductAlreadyExistsException(product.getName());

        return productRepository.save(product);
    }

    @Override
    public Product getProduct(Long id) {

        if (id == null)
            throw new ProductNotFoundException(id);

        Optional<Product> product = productRepository.findById(id);
        return unwrappedProduct(product, id);
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        Product existingProduct = this.getProduct(id);

        if (!existingProduct.getName().equals(product.getName())) {

            if (!existingProduct.getName().toLowerCase().equals(product.getName().toLowerCase())
                    && existsProductByName(product.getName()))
                throw new ProductAlreadyExistsException(product.getName());

            existingProduct.setName(product.getName());
        }

        existingProduct.setExpiryDate(product.getExpiryDate());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setPrice(product.getPrice());

        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        this.getProduct(id);
        productRepository.deleteById(id);
    }

    @Override
    public Set<Service> getServicesByProduct(Long id) {
        Product product = this.getProduct(id);
        return product.getServices();
    }

    private boolean existsProductByName(String name) {
        Optional<Product> product = productRepository.findByName(name);
        return product.isPresent();
    }

    static Product unwrappedProduct(Optional<Product> product, Long id) {

        if (product.isPresent())
            return product.get();

        throw new ProductNotFoundException(id);
    }

}
