package com.vasiu_catalina.beauty_salon.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.vasiu_catalina.beauty_salon.service.impl.ReviewServiceTest;
import com.vasiu_catalina.beauty_salon.entity.Review;
import com.vasiu_catalina.beauty_salon.service.IReviewService;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    @Mock
    private IReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    @Test
    void getAllReviewsReturnsOk() {
        List<Review> reviews = List.of(ReviewServiceTest.createReview());
        when(reviewService.getAllReviews()).thenReturn(reviews);

        ResponseEntity<List<Review>> response = reviewController.getAllReviews();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(reviews, response.getBody());
        verify(reviewService).getAllReviews();
    }

    @Test
    void createReviewReturnsCreated() {
        Review review = ReviewServiceTest.createReview();
        when(reviewService.createReview(2L, 1L, review)).thenReturn(review);

        ResponseEntity<Review> response = reviewController.createReview(2L, 1L, review);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(review, response.getBody());
        verify(reviewService).createReview(2L, 1L, review);
    }

    @Test
    void getReviewReturnsOk() {
        Review review = ReviewServiceTest.createReview();
        when(reviewService.getReview(2L, 1L)).thenReturn(review);

        ResponseEntity<Review> response = reviewController.getReview(2L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(review, response.getBody());
        verify(reviewService).getReview(2L, 1L);
    }

    @Test
    void updateReviewReturnsOk() {
        Review review = ReviewServiceTest.createReview();
        when(reviewService.updateReview(2L, 1L, review)).thenReturn(review);

        ResponseEntity<Review> response = reviewController.updateReview(2L, 1L, review);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(review, response.getBody());
        verify(reviewService).updateReview(2L, 1L, review);
    }

    @Test
    void deleteReviewReturnsNoContent() {
        ResponseEntity<HttpStatus> response = reviewController.deleteReview(2L, 1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(reviewService).deleteReview(2L, 1L);
    }

    @Test
    void getReviewsByClientReturnsOk() {
        Set<Review> reviews = Set.of(ReviewServiceTest.createReview());
        when(reviewService.getReviewsByClient(2L)).thenReturn(reviews);

        ResponseEntity<Set<Review>> response = reviewController.getReviewsByClient(2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(reviews, response.getBody());
        verify(reviewService).getReviewsByClient(2L);
    }

    @Test
    void getReviewsByEmployeeReturnsOk() {
        Set<Review> reviews = Set.of(ReviewServiceTest.createReview());
        when(reviewService.getReviewsByEmployee(1L)).thenReturn(reviews);

        ResponseEntity<Set<Review>> response = reviewController.getReviewsByEmployee(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(reviews, response.getBody());
        verify(reviewService).getReviewsByEmployee(1L);
    }
}
