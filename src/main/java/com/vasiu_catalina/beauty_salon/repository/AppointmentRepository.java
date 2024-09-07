package com.vasiu_catalina.beauty_salon.repository;

import org.springframework.data.repository.CrudRepository;

import com.vasiu_catalina.beauty_salon.entity.Appointment;

public interface AppointmentRepository extends CrudRepository<Appointment, Long> {
    
}
