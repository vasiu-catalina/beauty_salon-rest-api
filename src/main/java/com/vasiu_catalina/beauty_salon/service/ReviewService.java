package com.vasiu_catalina.beauty_salon.service;

import java.util.Set;

import com.vasiu_catalina.beauty_salon.entity.Review;

public interface ReviewService {
        
    Set<Review> getAllReviews();

    Review createReview(Review review);

    Review getReview(Long clientId, Long employeeId);

    Review updateReview(Long clientId, Long employeeId, Review review);

    void deleteReview(Long clientId, Long employeeId);

    Set<Review> getReviewsByClient(Long clientId);

    Set<Review> getReviewsByEmployee(Long employeeId);

}
