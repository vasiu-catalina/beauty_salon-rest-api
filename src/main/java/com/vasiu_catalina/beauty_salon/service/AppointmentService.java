package com.vasiu_catalina.beauty_salon.service;

import java.util.Set;

import com.vasiu_catalina.beauty_salon.entity.Appointment;

public interface AppointmentService {
    
    Set<Appointment> getAllAppointments();

    Appointment getAppointment(Long clientId, Long employeeId);

    Appointment createAppointment(Long clientId, Long employeeId, Appointment appointment);

    Appointment updateAppointment(Long clientId, Long employeeId, Appointment appointment);

    void deleteAppointment(Long clientId, Long employeeId);

    Set<Appointment> getAppointmentsByClient(Long clientId);

    Set<Appointment> getAppointmentsByEmployee(Long employeeId);
}
