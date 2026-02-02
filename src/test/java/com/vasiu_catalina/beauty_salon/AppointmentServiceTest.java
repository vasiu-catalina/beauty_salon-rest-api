package com.vasiu_catalina.beauty_salon;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vasiu_catalina.beauty_salon.entity.Appointment;
import com.vasiu_catalina.beauty_salon.entity.Client;
import com.vasiu_catalina.beauty_salon.entity.Employee;
import com.vasiu_catalina.beauty_salon.exception.appointment.AppointmentAlreadyExistsException;
import com.vasiu_catalina.beauty_salon.exception.appointment.AppointmentNotFoundException;
import com.vasiu_catalina.beauty_salon.exception.client.ClientNotFoundException;
import com.vasiu_catalina.beauty_salon.exception.employee.EmployeeNotFoundException;
import com.vasiu_catalina.beauty_salon.repository.AppointmentRepository;
import com.vasiu_catalina.beauty_salon.repository.ClientRepository;
import com.vasiu_catalina.beauty_salon.repository.EmployeeRepository;
import com.vasiu_catalina.beauty_salon.repository.ServiceRepository;
import com.vasiu_catalina.beauty_salon.service.impl.AppointmentServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Test
    public void testGetAllAppointments() {

        when(appointmentRepository.findAll()).thenReturn(List.of(createOtherAppointment(), createAppointment()));

        List<Appointment> result = appointmentService.getAllAppointments();

        assertEquals(2, result.size());
        assertEquals(LocalDateTime.of(2025, 10, 2, 15, 0, 0), result.get(0).getDate());
        assertEquals(new BigDecimal(0), result.get(1).getTotalPrice());
        verify(appointmentRepository, times(1)).findAll();
    }

    @Test
    public void testCreateAppointment() {

        Long clientId = 1L;
        Long employeeId = 1L;

        Appointment appointment = createAppointment();
        Client client = ClientServiceTest.createClient();
        Employee employee = EmployeeServiceTest.createEmployee();

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        Appointment result = appointmentService.createAppointment(clientId, employeeId, appointment);

        assertFullResult(result);
        verify(appointmentRepository, times(1)).save(appointment);
    }

    @Test
    public void testcreateAppointmentClientNotFoundException() {

        Long clientId = 1L;
        Long employeeId = 1L;
        Appointment appointment = createAppointment();

        ClientNotFoundException exception = assertThrows(ClientNotFoundException.class, () -> {
            appointmentService.createAppointment(clientId, employeeId, appointment);
        });

        ClientServiceTest.assertClientNotFoundException(exception, clientId);
        verify(appointmentRepository, never()).save(appointment);
    }

    @Test
    public void testcreateAppointmentEmployeeNotFoundException() {

        Long clientId = 1L;
        Long employeeId = 1L;
        Client client = ClientServiceTest.createClient();
        Appointment appointment = createAppointment();

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> {
            appointmentService.createAppointment(clientId, employeeId, appointment);
        });

        EmployeeServiceTest.assertEmployeeNotFoundException(exception, clientId);
        verify(appointmentRepository, never()).save(appointment);
    }

    @Test
    public void testcreateAppointmentAlreadyExistsByClientAndDate() {

        Long clientId = 1L;
        Long employeeId = 1L;
        Client client = ClientServiceTest.createClient();
        Appointment appointment = createAppointment();

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(appointmentRepository.existsByClientIdAndDate(clientId, appointment.getDate())).thenReturn(true);

        AppointmentAlreadyExistsException exception = assertThrows(AppointmentAlreadyExistsException.class, () -> {
            appointmentService.createAppointment(clientId, employeeId, appointment);
        });

        assertAppointmentAlreadyExistsException(exception, "client", clientId, appointment.getDate());
        verify(appointmentRepository, never()).save(appointment);
    }

    @Test
    public void testcreateAppointmentAlreadyExistsByEmployeeAndDate() {

        Long clientId = 1L;
        Long employeeId = 1L;

        Client client = ClientServiceTest.createClient();
        Employee employee = EmployeeServiceTest.createEmployee();
        Appointment appointment = createAppointment();

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(appointmentRepository.existsByClientIdAndDate(clientId, appointment.getDate())).thenReturn(false);
        when(appointmentRepository.existsByEmployeeIdAndDate(employeeId, appointment.getDate())).thenReturn(true);

        AppointmentAlreadyExistsException exception = assertThrows(AppointmentAlreadyExistsException.class, () -> {
            appointmentService.createAppointment(clientId, employeeId, appointment);
        });

        assertAppointmentAlreadyExistsException(exception, "employee", employeeId, appointment.getDate());
        verify(appointmentRepository, never()).save(appointment);
    }

    @Test
    public void testGetAppointment() {

        Long clientId = 1L;
        Long employeeId = 1L;
        LocalDateTime date = LocalDateTime.of(2025, 10, 1, 10, 0, 0);
        Appointment appointment = createAppointment();

        when(appointmentRepository.findByClientIdAndEmployeeIdAndDate(clientId, employeeId, date))
                .thenReturn(Optional.of(appointment));

        Appointment result = appointmentService.getAppointment(clientId, employeeId, date);

        assertFullResult(result);
        verify(appointmentRepository, times(1)).findByClientIdAndEmployeeIdAndDate(clientId, employeeId, date);
    }

    @Test
    public void testGetAppointmentNotFound() {

        Long clientId = 1L;
        Long employeeId = 1L;
        LocalDateTime date = LocalDateTime.of(2025, 10, 1, 10, 0, 0);

        when(appointmentRepository.findByClientIdAndEmployeeIdAndDate(clientId, employeeId, date))
                .thenReturn(Optional.empty());

        AppointmentNotFoundException exception = assertThrows(AppointmentNotFoundException.class, () -> {
            appointmentService.getAppointment(clientId, employeeId, date);
        });

        assertAppointmentNotFoundException(exception, clientId, employeeId, date);
        verify(appointmentRepository, times(1)).findByClientIdAndEmployeeIdAndDate(clientId, employeeId, date);
    }

    @Test
    public void updateAppointment() {
        Long clientId = 1L;
        Long employeeId = 1L;
        LocalDateTime date = LocalDateTime.of(2025, 10, 1, 10, 0, 0);
        Appointment appointment = createOtherAppointment();
        Appointment updatedAppointment = createAppointment();

        when(appointmentRepository.findByClientIdAndEmployeeIdAndDate(clientId, employeeId, date))
                .thenReturn(Optional.of(appointment));

        when(appointmentRepository.existsByClientIdAndDate(clientId, date))
                .thenReturn(false);

        when(appointmentRepository.existsByEmployeeIdAndDate(employeeId, date))
                .thenReturn(false);

        when(appointmentRepository.save(appointment))
                .thenReturn(updatedAppointment);

        Appointment result = appointmentService.updateAppointment(clientId, employeeId, date, updatedAppointment);

        assertFullResult(result);
        verify(appointmentRepository, times(1)).existsByClientIdAndDate(clientId, date);
        verify(appointmentRepository, times(1)).existsByEmployeeIdAndDate(employeeId, date);
        verify(appointmentRepository, times(1)).save(appointment);

    }

    @Test
    public void updateAppointmentEqualDates() {
        Long clientId = 1L;
        Long employeeId = 1L;
        LocalDateTime date = LocalDateTime.of(2025, 10, 1, 10, 0, 0);
        Appointment appointment = createOtherAppointment();
        Appointment updatedAppointment = createAppointment();
        appointment.setDate(updatedAppointment.getDate());

        when(appointmentRepository.findByClientIdAndEmployeeIdAndDate(clientId, employeeId, date))
                .thenReturn(Optional.of(appointment));

        when(appointmentRepository.save(appointment))
                .thenReturn(updatedAppointment);

        Appointment result = appointmentService.updateAppointment(clientId, employeeId, date, appointment);

        assertFullResult(result);
        verify(appointmentRepository, times(1)).save(appointment);

    }

    @Test
    public void updateAppointmentExistsByClientIdAndDate() {
        Long clientId = 1L;
        Long employeeId = 1L;
        LocalDateTime date = LocalDateTime.of(2025, 10, 1, 10, 0, 0);
        Appointment appointment = createOtherAppointment();
        Appointment updatedAppointment = createAppointment();

        when(appointmentRepository.findByClientIdAndEmployeeIdAndDate(clientId, employeeId, date))
                .thenReturn(Optional.of(appointment));

        when(appointmentRepository.existsByClientIdAndDate(clientId, date))
                .thenReturn(true);

        AppointmentAlreadyExistsException exception = assertThrows(AppointmentAlreadyExistsException.class, () -> {
            appointmentService.updateAppointment(clientId, employeeId, date, updatedAppointment);
        });

        assertAppointmentAlreadyExistsException(exception, "client", clientId, updatedAppointment.getDate());
        verify(appointmentRepository, times(1)).existsByClientIdAndDate(clientId, date);
        verify(appointmentRepository, never()).save(appointment);
    }

    @Test
    public void updateAppointmentExistsByEmployeeIdAndDate() {
        Long clientId = 1L;
        Long employeeId = 1L;
        LocalDateTime date = LocalDateTime.of(2025, 10, 1, 10, 0, 0);
        Appointment appointment = createOtherAppointment();
        Appointment updatedAppointment = createAppointment();

        when(appointmentRepository.findByClientIdAndEmployeeIdAndDate(clientId, employeeId, date))
                .thenReturn(Optional.of(appointment));

        when(appointmentRepository.existsByClientIdAndDate(clientId, date)).thenReturn(false);
        when(appointmentRepository.existsByEmployeeIdAndDate(clientId, date)).thenReturn(true);

        AppointmentAlreadyExistsException exception = assertThrows(AppointmentAlreadyExistsException.class, () -> {
            appointmentService.updateAppointment(clientId, employeeId, date, updatedAppointment);
        });

        assertAppointmentAlreadyExistsException(exception, "employee", employeeId, updatedAppointment.getDate());
        verify(appointmentRepository, times(1)).existsByClientIdAndDate(clientId, date);
        verify(appointmentRepository, never()).save(appointment);
    }

    @Test
    public void deleteAppointment() {
        Long clientId = 1L;
        Long employeeId = 1L;
        LocalDateTime date = LocalDateTime.of(2025, 10, 1, 10, 0, 0);
        Appointment appointment = createAppointment();

        when(appointmentRepository.findByClientIdAndEmployeeIdAndDate(clientId, employeeId, date))
                .thenReturn(Optional.of(appointment));

        appointmentService.deleteAppointment(clientId, employeeId, date);

        verify(appointmentRepository, times(1)).findByClientIdAndEmployeeIdAndDate(clientId, employeeId, date);
        verify(appointmentRepository, times(1)).deleteByClientIdAndEmployeeIdAndDate(clientId, employeeId, date);
    }

    @Test
    public void deleteAppointmentNotFoundException() {
        Long clientId = 1L;
        Long employeeId = 1L;
        LocalDateTime date = LocalDateTime.of(2025, 10, 1, 10, 0, 0);

        when(appointmentRepository.findByClientIdAndEmployeeIdAndDate(clientId, employeeId, date))
                .thenReturn(Optional.empty());

        AppointmentNotFoundException exception = assertThrows(AppointmentNotFoundException.class, () -> {
            appointmentService.deleteAppointment(clientId, employeeId, date);
        });

        assertAppointmentNotFoundException(exception, clientId, employeeId, date);
        verify(appointmentRepository, times(1)).findByClientIdAndEmployeeIdAndDate(clientId, employeeId, date);
        verify(appointmentRepository, never()).deleteByClientIdAndEmployeeIdAndDate(clientId, employeeId, date);
    }

    @Test
    public void getAppointmentsByClient() {

        Long clientId = 1L;
        Client client = ClientServiceTest.createClient();
        Iterable<Appointment> appointments = List.of(
            AppointmentServiceTest.createAppointment(),
            AppointmentServiceTest.createOtherAppointment()
        );

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(appointmentRepository.findByClientId(clientId)).thenReturn(appointments);

        List<Appointment> result = new ArrayList<>(appointmentService.getAppointmentsByClient(clientId));

        assertNotNull(result);
        assertEquals(appointments, result);
        verify(clientRepository, times(1)).findById(clientId);
        verify(appointmentRepository, times(1)).findByClientId(clientId);
    }


    @Test
    public void getAppointmentsByEmployee() {

        Long employeeId = 1L;
        Employee employee = EmployeeServiceTest.createEmployee();
        Iterable<Appointment> appointments = List.of(
            AppointmentServiceTest.createAppointment(),
            AppointmentServiceTest.createOtherAppointment()
        );

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(appointmentRepository.findByEmployeeId(employeeId)).thenReturn(appointments);

        List<Appointment> result = new ArrayList<>(appointmentService.getAppointmentsByEmployee(employeeId));

        assertNotNull(result);
        assertEquals(appointments, result);
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(appointmentRepository, times(1)).findByEmployeeId(employeeId);
    }

    @Test
    public void getAppointmentsByClientAndEmployee() {

        Long clientId = 1L;
        Long employeeId = 1L;
        
        Client client = ClientServiceTest.createClient();
        Employee employee = EmployeeServiceTest.createEmployee();
        Iterable<Appointment> appointments = List.of(
            AppointmentServiceTest.createAppointment(),
            AppointmentServiceTest.createOtherAppointment()
        );

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(appointmentRepository.findByClientIdAndEmployeeId(clientId, employeeId)).thenReturn(appointments);

        List<Appointment> result = new ArrayList<>(appointmentService.getAppointmentsByClientAndEmployee(clientId, employeeId));

        assertNotNull(result);
        assertEquals(appointments, result);
        verify(clientRepository, times(1)).findById(clientId);
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(appointmentRepository, times(1)).findByClientIdAndEmployeeId(clientId, employeeId);
    }


    private static void assertFullResult(Appointment result) {
        assertNotNull(result);
        assertEquals(LocalDateTime.of(2025, 10, 1, 10, 0, 0), result.getDate());
        assertEquals(new BigDecimal(0), result.getTotalPrice());
    }

    public static void assertAppointmentAlreadyExistsException(AppointmentAlreadyExistsException exception,
            String entityName, Long entityId, LocalDateTime date) {
        AppointmentAlreadyExistsException expected = new AppointmentAlreadyExistsException(entityName, entityId, date);
        assertEquals(expected.getMessage(), exception.getMessage());
    }

    public static void assertAppointmentNotFoundException(AppointmentNotFoundException exception,
            Long clientId, Long employeeId, LocalDateTime date) {
        AppointmentNotFoundException expected = new AppointmentNotFoundException(clientId, employeeId, date);
        assertEquals(expected.getMessage(), exception.getMessage());
    }

    public static Appointment createAppointment() {
        return new Appointment(LocalDateTime.of(2025, 10, 1, 10, 0, 0), new BigDecimal(0));
    }

    public static Appointment createOtherAppointment() {
        return new Appointment(LocalDateTime.of(2025, 10, 2, 15, 0, 0), new BigDecimal(0));
    }

}
