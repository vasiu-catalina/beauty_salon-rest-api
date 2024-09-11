package com.vasiu_catalina.beauty_salon.exception.appointment;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;

public class AppointmentAlreadyExistsException extends DataIntegrityViolationException {
    
    public AppointmentAlreadyExistsException(String entity, Long enitityId, LocalDateTime date) {
        super("Appointment for " + entity + " with ID " + enitityId + " on " + date.toString() + " already exists.");
    }
}
