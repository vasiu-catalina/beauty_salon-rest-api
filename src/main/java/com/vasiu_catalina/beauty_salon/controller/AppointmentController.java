package com.vasiu_catalina.beauty_salon.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vasiu_catalina.beauty_salon.entity.Appointment;
import com.vasiu_catalina.beauty_salon.service.IAppointmentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@Tag(name = "Appointments")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private IAppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return new ResponseEntity<>(appointmentService.getAllAppointments(), HttpStatus.OK);
    }

    @PostMapping("/employees/{employeeId}/clients/{clientId}")
    public ResponseEntity<Appointment> createAppointment(@PathVariable Long employeeId, @PathVariable Long clientId,
            @RequestBody @Valid Appointment appointment) {
        return new ResponseEntity<>(appointmentService.createAppointment(clientId, employeeId, appointment),
                HttpStatus.CREATED);
    }

    @GetMapping("/employees/{employeeId}/clients/{clientId}")
    public ResponseEntity<Object> getAppointment(@PathVariable Long employeeId, @PathVariable Long clientId,
            @RequestParam(required = false) LocalDateTime date) {
        Object responseBody = (date != null) ? appointmentService.getAppointment(clientId, employeeId, date) : 
        appointmentService.getAppointmentsByClientAndEmployee(clientId, employeeId);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PutMapping("/employees/{employeeId}/clients/{clientId}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable Long employeeId, @PathVariable Long clientId,
            @RequestParam LocalDateTime date, @RequestBody Appointment appointment) {
        return new ResponseEntity<>(appointmentService.updateAppointment(clientId, employeeId, date, appointment),
                HttpStatus.OK);
    }

    @DeleteMapping("/employees/{employeeId}/clients/{clientId}")
    public ResponseEntity<HttpStatus> deleteAppointment(@PathVariable Long employeeId, @PathVariable Long clientId,
            @RequestParam LocalDateTime date) {

        appointmentService.deleteAppointment(clientId, employeeId, date);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/clients/{clientId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByClient(@PathVariable Long clientId) {
        return new ResponseEntity<>(appointmentService.getAppointmentsByClient(clientId), HttpStatus.OK);
    }

    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByEmployee(@PathVariable Long employeeId) {
        return new ResponseEntity<>(appointmentService.getAppointmentsByEmployee(employeeId), HttpStatus.OK);
    }
}
