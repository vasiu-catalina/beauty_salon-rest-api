package com.vasiu_catalina.beauty_salon.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.vasiu_catalina.beauty_salon.service.impl.AppointmentServiceTest;
import com.vasiu_catalina.beauty_salon.entity.Appointment;
import com.vasiu_catalina.beauty_salon.service.IAppointmentService;

@ExtendWith(MockitoExtension.class)
class AppointmentControllerTest {

    @Mock
    private IAppointmentService appointmentService;

    @InjectMocks
    private AppointmentController appointmentController;

    @Test
    void getAllAppointmentsReturnsOk() {
        List<Appointment> appointments = List.of(AppointmentServiceTest.createAppointment());
        when(appointmentService.getAllAppointments()).thenReturn(appointments);

        ResponseEntity<List<Appointment>> response = appointmentController.getAllAppointments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(appointments, response.getBody());
        verify(appointmentService).getAllAppointments();
    }

    @Test
    void createAppointmentReturnsCreated() {
        Appointment appointment = AppointmentServiceTest.createAppointment();
        when(appointmentService.createAppointment(2L, 1L, appointment)).thenReturn(appointment);

        ResponseEntity<Appointment> response = appointmentController.createAppointment(1L, 2L, appointment);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(appointment, response.getBody());
        verify(appointmentService).createAppointment(2L, 1L, appointment);
    }

    @Test
    void getAppointmentWithDateReturnsOk() {
        LocalDateTime date = LocalDateTime.now().plusDays(1);
        Appointment appointment = AppointmentServiceTest.createAppointment();
        when(appointmentService.getAppointment(2L, 1L, date)).thenReturn(appointment);

        ResponseEntity<Object> response = appointmentController.getAppointment(1L, 2L, date);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(appointment, response.getBody());
        verify(appointmentService).getAppointment(2L, 1L, date);
    }

    @Test
    void getAppointmentWithoutDateReturnsOk() {
        List<Appointment> appointments = List.of(AppointmentServiceTest.createAppointment());
        when(appointmentService.getAppointmentsByClientAndEmployee(2L, 1L)).thenReturn(appointments);

        ResponseEntity<Object> response = appointmentController.getAppointment(1L, 2L, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(appointments, response.getBody());
        verify(appointmentService).getAppointmentsByClientAndEmployee(2L, 1L);
    }

    @Test
    void updateAppointmentReturnsOk() {
        LocalDateTime date = LocalDateTime.now().plusDays(1);
        Appointment appointment = AppointmentServiceTest.createAppointment();
        when(appointmentService.updateAppointment(2L, 1L, date, appointment)).thenReturn(appointment);

        ResponseEntity<Appointment> response = appointmentController.updateAppointment(1L, 2L, date, appointment);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(appointment, response.getBody());
        verify(appointmentService).updateAppointment(2L, 1L, date, appointment);
    }

    @Test
    void deleteAppointmentReturnsNoContent() {
        LocalDateTime date = LocalDateTime.now().plusDays(1);

        ResponseEntity<HttpStatus> response = appointmentController.deleteAppointment(1L, 2L, date);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(appointmentService).deleteAppointment(2L, 1L, date);
    }

    @Test
    void getAppointmentsByClientReturnsOk() {
        List<Appointment> appointments = List.of(AppointmentServiceTest.createAppointment());
        when(appointmentService.getAppointmentsByClient(2L)).thenReturn(appointments);

        ResponseEntity<List<Appointment>> response = appointmentController.getAppointmentsByClient(2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(appointments, response.getBody());
        verify(appointmentService).getAppointmentsByClient(2L);
    }

    @Test
    void getAppointmentsByEmployeeReturnsOk() {
        List<Appointment> appointments = List.of(AppointmentServiceTest.createAppointment());
        when(appointmentService.getAppointmentsByEmployee(1L)).thenReturn(appointments);

        ResponseEntity<List<Appointment>> response = appointmentController.getAppointmentsByEmployee(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(appointments, response.getBody());
        verify(appointmentService).getAppointmentsByEmployee(1L);
    }
}
