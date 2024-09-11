package com.vasiu_catalina.beauty_salon.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.vasiu_catalina.beauty_salon.entity.Appointment;

import jakarta.transaction.Transactional;

public interface AppointmentRepository extends CrudRepository<Appointment, Long> {

    Iterable<Appointment> findByClientIdAndEmployeeId(Long clientId, Long employeeId);
    
    Iterable<Appointment> findByClientId(Long clientId);
    
    Iterable<Appointment> findByEmployeeId(Long employeeId);

    boolean existsByClientIdAndDate(Long clientId, LocalDateTime date);
    
    boolean existsByEmployeeIdAndDate(Long employeeId, LocalDateTime date);

    Optional<Appointment> findByClientIdAndEmployeeIdAndDate(Long clientId, Long employeeId, LocalDateTime date);

    @Transactional
    void deleteByClientIdAndEmployeeIdAndDate(Long clientId, Long employeeId, LocalDateTime date);
}
