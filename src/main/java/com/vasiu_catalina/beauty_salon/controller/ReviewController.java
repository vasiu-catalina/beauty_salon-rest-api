package com.vasiu_catalina.beauty_salon.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vasiu_catalina.beauty_salon.entity.Review;
import com.vasiu_catalina.beauty_salon.service.IReviewService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private IReviewService reviewService;

    @GetMapping
    ResponseEntity<List<Review>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @PostMapping("/employees/{employeeId}/clients/{clientId}")
    ResponseEntity<Review> createReview(@PathVariable Long clientId, @PathVariable Long employeeId,
            @RequestBody @Valid Review review) {
        return new ResponseEntity<>(reviewService.createReview(clientId, employeeId, review), HttpStatus.CREATED);
    }

    @GetMapping("/employees/{employeeId}/clients/{clientId}")
    ResponseEntity<Review> getReview(@PathVariable Long clientId, @PathVariable Long employeeId) {
        return ResponseEntity.ok(reviewService.getReview(clientId, employeeId));
    }

    @PutMapping("/employees/{employeeId}/clients/{clientId}")
    ResponseEntity<Review> updateReview(@PathVariable Long clientId, @PathVariable Long employeeId,
            @RequestBody @Valid Review review) {
        return ResponseEntity.ok(reviewService.updateReview(clientId, employeeId, review));
    }

    @DeleteMapping("/employees/{employeeId}/clients/{clientId}")
    ResponseEntity<HttpStatus> deleteReview(@PathVariable Long clientId, @PathVariable Long employeeId) {
        reviewService.deleteReview(clientId, employeeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/clients/{clientId}")
    ResponseEntity<Set<Review>> getReviewsByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(reviewService.getReviewsByClient(clientId));
    }

    @GetMapping("/employees/{employeeId}")
    ResponseEntity<Set<Review>> getReviewsByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(reviewService.getReviewsByEmployee(employeeId));
    }

}
