package com.vasiu_catalina.beauty_salon.repository;

import org.springframework.data.repository.CrudRepository;

import com.vasiu_catalina.beauty_salon.entity.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {
    
} 
