package com.vasiu_catalina.beauty_salon.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import java.util.Optional;

import com.vasiu_catalina.beauty_salon.entity.Client;
import com.vasiu_catalina.beauty_salon.entity.Employee;
import com.vasiu_catalina.beauty_salon.entity.Review;
import com.vasiu_catalina.beauty_salon.exception.review.ReviewAlreadyExistsException;
import com.vasiu_catalina.beauty_salon.exception.review.ReviewNotFoundException;
import com.vasiu_catalina.beauty_salon.repository.ClientRepository;
import com.vasiu_catalina.beauty_salon.repository.EmployeeRepository;
import com.vasiu_catalina.beauty_salon.repository.ReviewRepository;
import com.vasiu_catalina.beauty_salon.service.IReviewService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ReviewServiceImpl implements IReviewService {

    private ReviewRepository reviewRepository;
    private ClientRepository clientRepository;
    private EmployeeRepository employeeRepository;

    @Override
    public List<Review> getAllReviews() {
        return (List<Review>) reviewRepository.findAll();
    }

    @Override
    public Review createReview(Long clientId, Long employeeId, Review review) {
        if(reviewRepository.existsByClientIdAndEmployeeId(clientId, employeeId)) 
            throw new ReviewAlreadyExistsException(clientId, employeeId);

        Client client = this.getClient(clientId);
        Employee employee = this.getEmployee(employeeId);

        review.setClient(client);
        review.setEmployee(employee);;
        return reviewRepository.save(review);
    }

    @Override
    public Review getReview(Long clientId, Long employeeId) {
        Optional<Review> review = reviewRepository.findByClientIdAndEmployeeId(clientId, employeeId);
        return unwrappedReview(review, clientId, employeeId);

    }

    @Override
    public Review updateReview(Long clientId, Long employeeId, Review review) {
        Review existingReview = this.getReview(clientId, employeeId);

        existingReview.setRating(review.getRating());
        existingReview.setDescription(review.getDescription());

        return reviewRepository.save(existingReview);
    }

    @Override
    public void deleteReview(Long clientId, Long employeeId) {
        this.getReview(clientId, employeeId);
        reviewRepository.deleteByClientIdAndEmployeeId(clientId, employeeId);
    }

    @Override
    public Set<Review> getReviewsByClient(Long clientId) {
        Client client = this.getClient(clientId);
        return client.getReviews();
    }

    @Override
    public Set<Review> getReviewsByEmployee(Long employeeId) {
        Employee employee = this.getEmployee(employeeId);
        return employee.getReviews();
    }

    private Client getClient(Long clientId) {
        Optional<Client> client = clientRepository.findById(clientId);
        return ClientServiceImpl.unwrappedClient(client, clientId);
    }

    private Employee getEmployee(Long employeeId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        return EmployeeServiceImpl.unwrappedEmployee(employee, employeeId);
    }

    static Review unwrappedReview(Optional<Review> review, Long clientId, Long employeeId) {
        if (review.isPresent())
            return review.get();

        throw new ReviewNotFoundException(clientId, employeeId);
    }

}
