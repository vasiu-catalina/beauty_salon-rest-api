package com.vasiu_catalina.beauty_salon.service;

import java.time.LocalDateTime;
import java.util.List;

import com.vasiu_catalina.beauty_salon.entity.Appointment;

public interface IAppointmentService {

    List<Appointment> getAllAppointments();

    Appointment getAppointment(Long clientId, Long employeeId, LocalDateTime date);

    Appointment createAppointment(Long clientId, Long employeeId, Appointment appointment);

    Appointment updateAppointment(Long clientId, Long employeeId, LocalDateTime date, Appointment appointment);

    void deleteAppointment(Long clientId, Long employeeId, LocalDateTime date);

    List<Appointment> getAppointmentsByClient(Long clientId);

    List<Appointment> getAppointmentsByEmployee(Long employeeId);

    List<Appointment> getAppointmentsByClientAndEmployee(Long clientId, Long employeeId);

}
