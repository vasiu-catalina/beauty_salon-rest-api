package com.vasiu_catalina.beauty_salon.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.vasiu_catalina.beauty_salon.entity.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);
    Optional<Employee> findByPhoneNumber(String phoneNumber);
    Iterable<Employee> findAllByServiceId(Long serviceId);
}
