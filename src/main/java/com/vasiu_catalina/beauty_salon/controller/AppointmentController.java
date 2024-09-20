package com.vasiu_catalina.beauty_salon.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vasiu_catalina.beauty_salon.entity.Appointment;
import com.vasiu_catalina.beauty_salon.service.IAppointmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

        @Operation(summary = "Retrieves appointments", description = "Provides a list of all appointments", security = @SecurityRequirement(name = "bearerAuth"), responses = {
                        @ApiResponse(responseCode = "200", description = "Successful retrieval of appointments", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Appointment.class)))),
                        @ApiResponse(responseCode = "403", description = "Forbidden - not authorized to access all appointments")
        })
        @PreAuthorize("hasRole('EMPLOYEE')")
        @GetMapping
        public ResponseEntity<List<Appointment>> getAllAppointments() {
                return new ResponseEntity<>(appointmentService.getAllAppointments(), HttpStatus.OK);
        }

        @Operation(summary = "Create a new appointment", description = "Allows an employee or the client themselves to create a new appointment", security = @SecurityRequirement(name = "bearerAuth"), responses = {
                        @ApiResponse(responseCode = "201", description = "Successfully created an appointment", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Appointment.class))),
                        @ApiResponse(responseCode = "403", description = "Forbidden - not authorized to create this appointment")
        })
        @PreAuthorize("hasRole('EMPLOYEE') || #clientId == authentication.details")
        @PostMapping("/employees/{employeeId}/clients/{clientId}")
        public ResponseEntity<Appointment> createAppointment(
                        @PathVariable Long employeeId,
                        @PathVariable Long clientId,
                        @RequestBody @Valid Appointment appointment) {
                return new ResponseEntity<>(appointmentService.createAppointment(clientId, employeeId, appointment),
                                HttpStatus.CREATED);
        }

        @Operation(summary = "Get specific appointment(s)", description = "Retrieve an appointment for a given employee and client, optionally by a specific date", security = @SecurityRequirement(name = "bearerAuth"), responses = {
                        @ApiResponse(responseCode = "200", description = "Successfully retrieved appointment(s)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Appointment.class))),
                        @ApiResponse(responseCode = "404", description = "Appointment not found")
        })
        @PreAuthorize("hasRole('EMPLOYEE') || #clientId == authentication.details")
        @GetMapping("/employees/{employeeId}/clients/{clientId}")
        public ResponseEntity<Object> getAppointment(
                        @PathVariable Long employeeId,
                        @PathVariable Long clientId,
                        @RequestParam(required = false) LocalDateTime date) {
                Object responseBody = (date != null)
                                ? appointmentService.getAppointment(clientId, employeeId, date)
                                : appointmentService.getAppointmentsByClientAndEmployee(clientId, employeeId);
                return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }

        @Operation(summary = "Update an appointment", description = "Allows an employee or client to update an existing appointment", security = @SecurityRequirement(name = "bearerAuth"), responses = {
                        @ApiResponse(responseCode = "200", description = "Successfully updated the appointment", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Appointment.class))),
                        @ApiResponse(responseCode = "404", description = "Appointment not found")
        })
        @PreAuthorize("hasRole('EMPLOYEE') || #clientId == authentication.details")
        @PutMapping("/employees/{employeeId}/clients/{clientId}")
        public ResponseEntity<Appointment> updateAppointment(
                        @PathVariable Long employeeId,
                        @PathVariable Long clientId,
                        @RequestParam LocalDateTime date,
                        @RequestBody Appointment appointment) {
                return new ResponseEntity<>(
                                appointmentService.updateAppointment(clientId, employeeId, date, appointment),
                                HttpStatus.OK);
        }

        @Operation(summary = "Delete an appointment", description = "Allows an employee or client to delete an appointment by date", security = @SecurityRequirement(name = "bearerAuth"), responses = {
                        @ApiResponse(responseCode = "204", description = "Successfully deleted the appointment"),
                        @ApiResponse(responseCode = "404", description = "Appointment not found")
        })
        @PreAuthorize("hasRole('EMPLOYEE') || #clientId == authentication.details")
        @DeleteMapping("/employees/{employeeId}/clients/{clientId}")
        public ResponseEntity<HttpStatus> deleteAppointment(
                        @PathVariable Long employeeId,
                        @PathVariable Long clientId,
                        @RequestParam LocalDateTime date) {
                appointmentService.deleteAppointment(clientId, employeeId, date);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        @Operation(summary = "Get all appointments for a client", description = "Allows an employee or client to retrieve all appointments for a specific client", security = @SecurityRequirement(name = "bearerAuth"), responses = {
                        @ApiResponse(responseCode = "200", description = "Successfully retrieved appointments", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Appointment.class))))
        })
        @PreAuthorize("hasRole('EMPLOYEE') || #clientId == authentication.details")
        @GetMapping("/clients/{clientId}")
        public ResponseEntity<List<Appointment>> getAppointmentsByClient(@PathVariable Long clientId) {
                return new ResponseEntity<>(appointmentService.getAppointmentsByClient(clientId), HttpStatus.OK);
        }

        @Operation(summary = "Get all appointments for an employee", description = "Retrieve all appointments assigned to a specific employee", security = @SecurityRequirement(name = "bearerAuth"), responses = {
                        @ApiResponse(responseCode = "200", description = "Successfully retrieved appointments", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Appointment.class))))
        })
        @PreAuthorize("hasRole('EMPLOYEE')")
        @GetMapping("/employees/{employeeId}")
        public ResponseEntity<List<Appointment>> getAppointmentsByEmployee(@PathVariable Long employeeId) {
                return new ResponseEntity<>(appointmentService.getAppointmentsByEmployee(employeeId), HttpStatus.OK);
        }
}