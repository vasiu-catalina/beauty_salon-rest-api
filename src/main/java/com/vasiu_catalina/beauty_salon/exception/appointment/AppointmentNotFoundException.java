package com.vasiu_catalina.beauty_salon.exception.appointment;

import java.time.LocalDateTime;

public class AppointmentNotFoundException extends RuntimeException {
    
    public AppointmentNotFoundException(Long clientId, Long employeeId, LocalDateTime date) {
        super("Appointments with Client ID " + clientId + " and Employee ID on "+ date + " was not found.");
    }
}
