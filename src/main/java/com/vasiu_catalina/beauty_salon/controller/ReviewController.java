package com.vasiu_catalina.beauty_salon.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Tag(name = "Reviews")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

        private IReviewService reviewService;

        @Operation(summary = "Get all reviews", description = "Retrieves a list of all reviews.", security = @SecurityRequirement(name = "bearerAuth"))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Successfully retrieved all reviews", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
        })
        @PreAuthorize("hasRole('EMPLOYEE')")
        @GetMapping
        public ResponseEntity<List<Review>> getAllReviews() {
                return ResponseEntity.ok(reviewService.getAllReviews());
        }

        @Operation(summary = "Create a review", description = "Allows a client to create a review for an employee.", security = @SecurityRequirement(name = "bearerAuth"))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Successfully created review", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized to create review")
        })
        @PreAuthorize("hasRole('CLIENT') && #clientId == authentication.details")
        @PostMapping("/employees/{employeeId}/clients/{clientId}")
        public ResponseEntity<Review> createReview(@PathVariable Long clientId, @PathVariable Long employeeId,
                        @RequestBody @Valid Review review) {
                return new ResponseEntity<>(reviewService.createReview(clientId, employeeId, review),
                                HttpStatus.CREATED);
        }

        @Operation(summary = "Get a review", description = "Retrieves a specific review for an employee by a client.", security = @SecurityRequirement(name = "bearerAuth"))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Successfully retrieved review", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "404", description = "Review not found")
        })
        @GetMapping("/employees/{employeeId}/clients/{clientId}")
        public ResponseEntity<Review> getReview(@PathVariable Long clientId, @PathVariable Long employeeId) {
                return ResponseEntity.ok(reviewService.getReview(clientId, employeeId));
        }

        @Operation(summary = "Update a review", description = "Updates a specific review by a client for an employee.", security = @SecurityRequirement(name = "bearerAuth"))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Successfully updated review", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized to update review"),
                        @ApiResponse(responseCode = "404", description = "Review not found")
        })
        @PreAuthorize("hasRole('CLIENT') && #clientId == authentication.details")
        @PutMapping("/employees/{employeeId}/clients/{clientId}")
        public ResponseEntity<Review> updateReview(@PathVariable Long clientId, @PathVariable Long employeeId,
                        @RequestBody @Valid Review review) {
                return ResponseEntity.ok(reviewService.updateReview(clientId, employeeId, review));
        }

        @Operation(summary = "Delete a review", description = "Deletes a specific review by a client for an employee.", security = @SecurityRequirement(name = "bearerAuth"))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Successfully deleted review"),
                        @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized to delete review"),
                        @ApiResponse(responseCode = "404", description = "Review not found")
        })
        @PreAuthorize("hasRole('CLIENT') && #clientId == authentication.details")
        @DeleteMapping("/employees/{employeeId}/clients/{clientId}")
        public ResponseEntity<HttpStatus> deleteReview(@PathVariable Long clientId, @PathVariable Long employeeId) {
                reviewService.deleteReview(clientId, employeeId);
                return ResponseEntity.noContent().build();
        }

        @Operation(summary = "Get reviews by client", description = "Retrieves all reviews associated with a specific client.", security = @SecurityRequirement(name = "bearerAuth"))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Successfully retrieved reviews for client", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
        })
        @PreAuthorize("hasRole('EMPLOYEE') || #clientId == authentication.details")
        @GetMapping("/clients/{clientId}")
        public ResponseEntity<Set<Review>> getReviewsByClient(@PathVariable Long clientId) {
                return ResponseEntity.ok(reviewService.getReviewsByClient(clientId));
        }

        @Operation(summary = "Get reviews by employee", description = "Retrieves all reviews associated with a specific employee.", security = @SecurityRequirement(name = "bearerAuth"))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Successfully retrieved reviews for employee", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
        })
        @PreAuthorize("hasRole('EMPLOYEE')")
        @GetMapping("/employees/{employeeId}")
        public ResponseEntity<Set<Review>> getReviewsByEmployee(@PathVariable Long employeeId) {
                return ResponseEntity.ok(reviewService.getReviewsByEmployee(employeeId));
        }

}
