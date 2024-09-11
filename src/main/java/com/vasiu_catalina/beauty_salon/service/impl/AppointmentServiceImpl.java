package com.vasiu_catalina.beauty_salon.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.vasiu_catalina.beauty_salon.entity.Appointment;
import com.vasiu_catalina.beauty_salon.entity.AppointmentService;
import com.vasiu_catalina.beauty_salon.entity.Client;
import com.vasiu_catalina.beauty_salon.entity.Employee;
import com.vasiu_catalina.beauty_salon.entity.Service;
import com.vasiu_catalina.beauty_salon.exception.appointment.AppointmentAlreadyExistsException;
import com.vasiu_catalina.beauty_salon.exception.appointment.AppointmentNotFoundException;
import com.vasiu_catalina.beauty_salon.exception.client.ClientNotFoundException;
import com.vasiu_catalina.beauty_salon.exception.employee.EmployeeNotFoundException;
import com.vasiu_catalina.beauty_salon.repository.AppointmentRepository;
import com.vasiu_catalina.beauty_salon.repository.AppointmentServiceRepository;
import com.vasiu_catalina.beauty_salon.repository.ClientRepository;
import com.vasiu_catalina.beauty_salon.repository.EmployeeRepository;
import com.vasiu_catalina.beauty_salon.repository.ServiceRepository;
import com.vasiu_catalina.beauty_salon.service.IAppointmentService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@org.springframework.stereotype.Service
public class AppointmentServiceImpl implements IAppointmentService {

    private AppointmentRepository appointmentRepository;
    private EmployeeRepository employeeRepository;
    private ClientRepository clientRepository;
    private ServiceRepository serviceRepository;
    private AppointmentServiceRepository appointmentServiceRepository;

    @Override
    public List<Appointment> getAllAppointments() {
        return (List<Appointment>) appointmentRepository.findAll();
    }

    @Override
    public Appointment getAppointment(Long clientId, Long employeeId, LocalDateTime date) {
        Optional<Appointment> appointment = appointmentRepository.findByClientIdAndEmployeeIdAndDate(clientId,
                employeeId, date);
        return unwrappedAppointment(appointment, clientId, employeeId, date);
    }

    @Override
    @Transactional
    public Appointment createAppointment(Long clientId, Long employeeId, Appointment appointment) {

        LocalDateTime date = appointment.getDate();

        Client client = this.getClient(clientId);
        if (appointmentRepository.existsByClientIdAndDate(clientId, date))
            throw new AppointmentAlreadyExistsException("client", clientId, date);

        Employee employee = this.getEmployee(employeeId);
        if (appointmentRepository.existsByEmployeeIdAndDate(employeeId, date))
            throw new AppointmentAlreadyExistsException("employee", employeeId, date);

            
            Set<AppointmentService> appointmentServices = 
            appointment.getServices()
            .stream()
            .map(service -> {
                AppointmentService appointmentService = new AppointmentService();
                Long serviceId = service.getService().getId();
                
                Optional<Service> optionalService = serviceRepository.findById(serviceId);
                Service unwrappedService = ServiceServiceImpl.unwrappedService(optionalService, serviceId);
                
                appointmentService.setServicePrice(unwrappedService.getPrice());
                appointmentService.setAppointment(appointment);
                appointmentService.setService(unwrappedService);
                
                return appointmentService;
            }).collect(Collectors.toSet());
            
            BigDecimal totalPrice = appointmentServices.stream().map(service -> service.getServicePrice()).reduce(BigDecimal.ZERO, BigDecimal::add);
        
        appointment.setClient(client);
        appointment.setEmployee(employee);
        appointment.setTotalPrice(totalPrice);
        appointment.setServices(appointmentServices);

        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment updateAppointment(Long clientId, Long employeeId, LocalDateTime date, Appointment appointment) {

        Optional<Appointment> optionalAppointment = appointmentRepository.findByClientIdAndEmployeeIdAndDate(clientId,
                employeeId, date);
        Appointment existingAppointment = unwrappedAppointment(optionalAppointment, clientId, employeeId, date);

        if (!existingAppointment.getDate().equals(appointment.getDate())) {
            if (appointmentRepository.existsByClientIdAndDate(clientId, date))
                throw new AppointmentAlreadyExistsException("client", clientId, date);

            if (appointmentRepository.existsByEmployeeIdAndDate(employeeId, date))
                throw new AppointmentAlreadyExistsException("employee", employeeId, date);

            existingAppointment.setDate(appointment.getDate());
        }
            
        Set<AppointmentService> appointmentServices = 
        appointment.getServices()
        .stream()
        .map(service -> {
            AppointmentService appointmentService = new AppointmentService();
            Long serviceId = service.getService().getId();
            
            Optional<Service> optionalService = serviceRepository.findById(serviceId);
            Service unwrappedService = ServiceServiceImpl.unwrappedService(optionalService, serviceId);
            
            appointmentService.setServicePrice(unwrappedService.getPrice());
            appointmentService.setAppointment(appointment);
            appointmentService.setService(unwrappedService);
            
            return appointmentService;
        }).collect(Collectors.toSet());
        
        BigDecimal totalPrice = appointmentServices.stream().map(service -> service.getServicePrice()).reduce(BigDecimal.ZERO, BigDecimal::add);
        
        existingAppointment.setTotalPrice(totalPrice);
        existingAppointment.setServices(appointmentServices);

        return appointmentRepository.save(existingAppointment);
    }

    @Override
    public void deleteAppointment(Long clientId, Long employeeId, LocalDateTime date) {
        appointmentRepository.deleteByClientIdAndEmployeeIdAndDate(clientId, employeeId, date);
    }

    @Override
    public List<Appointment> getAppointmentsByClient(Long clientId) {
        return (List<Appointment>) appointmentRepository.findByClientId(clientId);
    }

    @Override
    public List<Appointment> getAppointmentsByEmployee(Long employeeId) {
        return (List<Appointment>) appointmentRepository.findByEmployeeId(employeeId);
    }

    @Override
    public List<Appointment> getAppointmentsByClientAndEmployee(Long clientId, Long employeeId) {
        return (List<Appointment>) appointmentRepository.findByClientIdAndEmployeeId(clientId, employeeId);
    }

    private Client getClient(Long clientId) {
        return clientRepository.findById(clientId).orElseThrow(() -> new ClientNotFoundException(clientId));
    }

    private Employee getEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException(employeeId));
    }

    static Appointment unwrappedAppointment(Optional<Appointment> appointment, Long clientId, Long employeeId,
            LocalDateTime date) {
        if (appointment.isPresent())
            return appointment.get();
        throw new AppointmentNotFoundException(clientId, employeeId, date);
    }

}
