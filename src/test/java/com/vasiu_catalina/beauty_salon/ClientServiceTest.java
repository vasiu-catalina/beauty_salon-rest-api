package com.vasiu_catalina.beauty_salon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.vasiu_catalina.beauty_salon.entity.Client;
import com.vasiu_catalina.beauty_salon.exception.client.ClientAlreadyExistsException;
import com.vasiu_catalina.beauty_salon.exception.client.ClientNotFoundException;
import com.vasiu_catalina.beauty_salon.repository.ClientRepository;
import com.vasiu_catalina.beauty_salon.service.impl.ClientServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    public void testGetAllClients() {

        when(clientRepository.findAll()).thenReturn(List.of(createClient(), createOtherClient()));

        List<Client> result = clientService.getAllClients();

        assertEquals(2, result.size());
        assertEquals("Catalina", result.get(0).getFirstName());
        assertEquals("nume@gmail.com", result.get(1).getEmail());
    }

    @Test
    public void testCreateClient() {

        Client client = createClient();
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        Client result = clientService.createClient(client);

        assertFullResult(result);
        verify(clientRepository, times(1)).save(client);

    }

    @Test
    public void testEmailAlreadyTaken() {
        Client client = createClient();

        when(clientRepository.findByEmail(client.getEmail())).thenReturn(Optional.of(client));

        ClientAlreadyExistsException exception = assertThrows(ClientAlreadyExistsException.class, () -> {
            clientService.createClient(client);
        });

        assertClientAlreadyExistsException(exception, "Email");
        verify(clientRepository, times(1)).findByEmail(client.getEmail());
    }

    @Test
    public void testPhoneNumberAlreadyTaken() {
        Client client = createClient();
        when(clientRepository.findByPhoneNumber(client.getPhoneNumber())).thenReturn(Optional.of(client));

        ClientAlreadyExistsException exception = assertThrows(ClientAlreadyExistsException.class, () -> {
            clientService.createClient(client);
        });

        assertClientAlreadyExistsException(exception, "Phone number");
        verify(clientRepository, times(1)).findByPhoneNumber(client.getPhoneNumber());
    }

    @Test
    public void testGetClient() {

        Long clientId = 1L;
        Client client = createClient();
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        Client result = clientService.getClient(clientId);

        assertFullResult(result);
        verify(clientRepository, times(1)).findById(clientId);
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    public void testClientNotFound() {
        Long clientId = 1L;

        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        ClientNotFoundException exception = assertThrows(ClientNotFoundException.class, () -> {
            clientService.getClient(clientId);
        });

        assertClientNotFoundException(exception, clientId);
        verify(clientRepository, times(1)).findById(clientId);
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    public void testUpdateClient() {
        Long clientId = 1L;
        Client existingClient = createOtherClient();
        Client updatedClient = createClient();

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(existingClient));
        when(clientRepository.save(any(Client.class))).thenReturn(updatedClient);

        Client result = clientService.updateClient(clientId, updatedClient);

        assertFullResult(result);
        verify(clientRepository, times(1)).findById(clientId);
        verify(clientRepository, times(1)).save(existingClient);
    }

    @Test
    public void testUpdateClientNotFound() {
        Long clientId = 1L;
        Client updatedClient = createClient();

        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        ClientNotFoundException exception = assertThrows(ClientNotFoundException.class, () -> {
            clientService.updateClient(clientId, updatedClient);
        });

        assertClientNotFoundException(exception, clientId);
        verify(clientRepository, times(1)).findById(clientId);
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    public void testDeleteClient() {
        // arrange
        Long clientId = 1L;
        Client existingClient = createClient();

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(existingClient));

        // act
        clientService.deleteClient(clientId);

        // assert
        verify(clientRepository, times(1)).findById(clientId);
        verify(clientRepository, times(1)).deleteById(clientId);
    }

    @Test
    public void testdeleteClientNotFound() {
        Long clientId = 1L;

        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        ClientNotFoundException exception = assertThrows(ClientNotFoundException.class, () -> {
            clientService.deleteClient(clientId);
        });

        assertClientNotFoundException(exception, clientId);
        verify(clientRepository, times(1)).findById(clientId);
        verify(clientRepository, never()).deleteById(clientId);
    }

    private static void assertFullResult(Client result) {
        assertNotNull(result);
        assertEquals("Catalina", result.getFirstName());
        assertEquals("Vasiu", result.getLastName());
        assertEquals("cata@gmail.com", result.getEmail());
        assertEquals("+4071xxxxxxx", result.getPhoneNumber());
        assertEquals("Str. Anonim Nr. 0", result.getAddress());
        assertEquals(LocalDate.of(2000, 1, 1), result.getBirthDate());
    }

    public static void assertClientAlreadyExistsException(ClientAlreadyExistsException exception,
            String uniqueFieldName) {
        ClientAlreadyExistsException expected = new ClientAlreadyExistsException(uniqueFieldName);
        assertEquals(expected.getMessage(), exception.getMessage());
    }

    public static Client createClient() {
        return new Client("Catalina", "Vasiu", "cata@gmail.com", "+4071xxxxxxx", "Str. Anonim Nr. 0",
                LocalDate.of(2000, 1, 1));
    }

    public static Client createOtherClient() {
        return new Client("Prenume", "Nume", "nume@gmail.com", "+4072xxxxxxx", "Str. Random Nr. 1",
                LocalDate.of(2001, 2, 2));
    }

    public static void assertClientNotFoundException(ClientNotFoundException exception, Long clientId) {
        ClientNotFoundException expected = new ClientNotFoundException(clientId);
        assertEquals(expected.getMessage(), exception.getMessage());
    }

}
