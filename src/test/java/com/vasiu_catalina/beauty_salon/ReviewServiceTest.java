package com.vasiu_catalina.beauty_salon;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.vasiu_catalina.beauty_salon.entity.Client;
import com.vasiu_catalina.beauty_salon.entity.Employee;
import com.vasiu_catalina.beauty_salon.entity.Review;
import com.vasiu_catalina.beauty_salon.exception.review.ReviewAlreadyExistsException;
import com.vasiu_catalina.beauty_salon.exception.review.ReviewNotFoundException;
import com.vasiu_catalina.beauty_salon.repository.ClientRepository;
import com.vasiu_catalina.beauty_salon.repository.EmployeeRepository;
import com.vasiu_catalina.beauty_salon.repository.ReviewRepository;
import com.vasiu_catalina.beauty_salon.service.impl.ReviewServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Test
    public void testGetAllReviews() {

        when(reviewRepository.findAll()).thenReturn(List.of(createReview(), createOtherReview()));

        List<Review> result = reviewService.getAllReviews();

        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(5), result.get(0).getRating());
        assertEquals(Integer.valueOf(2), result.get(1).getRating());
        verify(reviewRepository, times(1)).findAll();
    }

    @Test
    public void testCreateReview() {

        Long clientId = 1L;
        Long employeeId = 1L;
        Review review = createReview();
        Client client = ClientServiceTest.createClient();
        Employee employee = EmployeeServiceTest.createEmployee();

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        Review result = reviewService.createReview(clientId, employeeId, review);

        assertFullResult(result);
        verify(reviewRepository, times(1)).save(any(Review.class));

    }

    @Test
    public void testReviewAlreadyExistsForClientByEmployee() {

        Long clientId = 1L;
        Long employeeId = 1L;
        Review review = createReview();

        when(reviewRepository.existsByClientIdAndEmployeeId(clientId, employeeId)).thenReturn(true);

        ReviewAlreadyExistsException exception = assertThrows(ReviewAlreadyExistsException.class, () -> {
            reviewService.createReview(clientId, employeeId, review);
        });

        assertReviewAlreadyExistsException(exception, clientId, employeeId);
        verify(reviewRepository, times(1)).existsByClientIdAndEmployeeId(clientId, employeeId);
        verify(reviewRepository, never()).save(review);
    }

    @Test
    public void testGetReview() {

        Long clientId = 1L;
        Long employeeId = 1L;
        Review review = createReview();

        when(reviewRepository.findByClientIdAndEmployeeId(clientId, employeeId)).thenReturn(Optional.of(review));

        Review result = reviewService.getReview(clientId, employeeId);

        assertFullResult(result);
        verify(reviewRepository, times(1)).findByClientIdAndEmployeeId(clientId, employeeId);
    }

    @Test
    public void testReviewNotFound() {
        Long clientId = 1L;
        Long employeeId = 1L;

        when(reviewRepository.findByClientIdAndEmployeeId(clientId, employeeId)).thenReturn(Optional.empty());

        ReviewNotFoundException exception = assertThrows(ReviewNotFoundException.class, () -> {
            reviewService.getReview(clientId, employeeId);
        });

        assertReviewNotFoundException(exception, clientId, employeeId);
        verify(reviewRepository, times(1)).findByClientIdAndEmployeeId(clientId, employeeId);
    }

    @Test
    public void testUpdateReview() {
        Long clientId = 1L;
        Long employeeId = 1L;
        Review review = createOtherReview();
        Review updatedReview = createReview();
        review.setDescription("Destul de multumita.");

        when(reviewRepository.findByClientIdAndEmployeeId(clientId, employeeId)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenReturn(updatedReview);

        Review result = reviewService.updateReview(clientId, employeeId, updatedReview);

        assertFullResult(result);
        verify(reviewRepository, times(1)).findByClientIdAndEmployeeId(clientId, employeeId);
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    public void testUpdateReviewNotFound() {

        Long clientId = 1L;
        Long employeeId = 1L;
        Review updatedReview = createReview();

        when(reviewRepository.findByClientIdAndEmployeeId(clientId, employeeId)).thenReturn(Optional.empty());

        ReviewNotFoundException exception = assertThrows(ReviewNotFoundException.class, () -> {
            reviewService.updateReview(clientId, employeeId, updatedReview);
        });

        ReviewNotFoundException expectedException = new ReviewNotFoundException(clientId, employeeId);

        assertEquals(expectedException.getMessage(), exception.getMessage());

        verify(reviewRepository, times(1)).findByClientIdAndEmployeeId(clientId, employeeId);
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    public void testDeleteReview() {

        Long clientId = 1L;
        Long employeeId = 1L;
        Review existingReview = new Review(5);

        when(reviewRepository.findByClientIdAndEmployeeId(clientId, employeeId)).thenReturn(Optional.of(existingReview));

        reviewService.deleteReview(clientId, employeeId);

        verify(reviewRepository, times(1)).findByClientIdAndEmployeeId(clientId, employeeId);
        verify(reviewRepository, times(1)).deleteByClientIdAndEmployeeId(clientId, employeeId);
    }

    @Test
    public void testdeleteReviewNotFound() {
        Long clientId = 1L;
        Long employeeId = 1L;

        when(reviewRepository.findByClientIdAndEmployeeId(clientId, employeeId)).thenReturn(Optional.empty());

        ReviewNotFoundException exception = assertThrows(ReviewNotFoundException.class, () -> {
            reviewService.deleteReview(clientId, employeeId);
        });

        assertReviewNotFoundException(exception, clientId, employeeId);
        verify(reviewRepository, times(1)).findByClientIdAndEmployeeId(clientId, employeeId);
        verify(reviewRepository, never()).deleteByClientIdAndEmployeeId(clientId, employeeId);
    }

    @Test
    public void testGetReviewsByClient() {

        Long clientId = 1L;
        Client existingClient = ClientServiceTest.createClient();
        Set<Review> expectedReviews = new HashSet<>(List.of(createReview(), createOtherReview()));
        existingClient.setReviews(expectedReviews);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(existingClient));

        Set<Review> result = reviewService.getReviewsByClient(clientId);

        assertEquals(expectedReviews, result);
        verify(clientRepository, times(1)).findById(clientId);

    }

    @Test
    public void testGetReviewsByEmployee() {

        Long employeeId = 1L;
        Employee existingEmployee = EmployeeServiceTest.createEmployee();
        Set<Review> expectedReviews = new HashSet<>(List.of(createReview(), createOtherReview()));
        existingEmployee.setReviews(expectedReviews);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));

        Set<Review> result = reviewService.getReviewsByEmployee(employeeId);

        assertEquals(expectedReviews, result);
        verify(employeeRepository, times(1)).findById(employeeId);

    }

    public static Review createReview() {
        return new Review(5);
    }

    public static Review createOtherReview() {
        return new Review(2);
    }

    public static void assertFullResult(Review result) {
        assertNotNull(result);
        assertEquals(Integer.valueOf(5), result.getRating());
        assertNull(result.getDescription());
    }

    public static void assertReviewAlreadyExistsException(ReviewAlreadyExistsException exception, Long clientId,
            Long employeeId) {
        ReviewAlreadyExistsException expectedException = new ReviewAlreadyExistsException(clientId, employeeId);
        assertEquals(expectedException.getMessage(), exception.getMessage());
    }

    public static void assertReviewNotFoundException(ReviewNotFoundException exception, Long clientId,
            Long employeeId) {
        ReviewNotFoundException expectedException = new ReviewNotFoundException(clientId, employeeId);
        assertEquals(expectedException.getMessage(), exception.getMessage());
    }

}
