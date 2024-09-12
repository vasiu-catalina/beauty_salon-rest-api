package com.vasiu_catalina.beauty_salon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.vasiu_catalina.beauty_salon.entity.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {
    
    @Query("SELECT p FROM Product p WHERE p.name LIKE :name")
    Optional<Product> findByName(@Param("name") String name);
}
