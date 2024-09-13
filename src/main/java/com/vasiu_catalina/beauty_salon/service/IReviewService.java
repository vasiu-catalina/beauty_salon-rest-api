package com.vasiu_catalina.beauty_salon.service;

import java.util.List;
import java.util.Set;

import com.vasiu_catalina.beauty_salon.entity.Review;

public interface IReviewService {
        
    List<Review> getAllReviews();

    Review createReview(Long clientId, Long employeeId, Review review);

    Review getReview(Long clientId, Long employeeId);

    Review updateReview(Long clientId, Long employeeId, Review review);

    void deleteReview(Long clientId, Long employeeId);

    Set<Review> getReviewsByClient(Long clientId);

    Set<Review> getReviewsByEmployee(Long employeeId);

}
