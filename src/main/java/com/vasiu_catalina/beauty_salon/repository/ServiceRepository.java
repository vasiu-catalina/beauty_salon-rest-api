package com.vasiu_catalina.beauty_salon.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.vasiu_catalina.beauty_salon.entity.Service;

public interface ServiceRepository extends CrudRepository<Service, Long> {

    Optional<Service> findByName(String name);
}
