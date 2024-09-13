package com.vasiu_catalina.beauty_salon.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.vasiu_catalina.beauty_salon.entity.Review;

import jakarta.transaction.Transactional;

public interface ReviewRepository extends CrudRepository<Review, Long>{
    
    Optional<Review> findByClientIdAndEmployeeId(Long clientId, Long employeeId);

    boolean existsByClientIdAndEmployeeId(Long clientId, Long employeeId);

    @Transactional
    void deleteByClientIdAndEmployeeId(Long clientId, Long emplyoeeId);
}
