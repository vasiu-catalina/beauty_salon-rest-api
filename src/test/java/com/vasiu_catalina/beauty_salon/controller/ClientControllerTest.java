package com.vasiu_catalina.beauty_salon.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.vasiu_catalina.beauty_salon.service.impl.ClientServiceTest;
import com.vasiu_catalina.beauty_salon.entity.Client;
import com.vasiu_catalina.beauty_salon.service.IClientService;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    @Mock
    private IClientService clientService;

    @InjectMocks
    private ClientController clientController;

    @Test
    void getAllClientsReturnsOk() {
        List<Client> clients = List.of(ClientServiceTest.createClient(), ClientServiceTest.createOtherClient());
        when(clientService.getAllClients()).thenReturn(clients);

        ResponseEntity<List<Client>> response = clientController.getAllClients();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(clients, response.getBody());
        verify(clientService).getAllClients();
    }

    @Test
    void createClientReturnsCreated() {
        Client client = ClientServiceTest.createClient();
        when(clientService.createClient(client)).thenReturn(client);

        ResponseEntity<Client> response = clientController.createClient(client);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(client, response.getBody());
        verify(clientService).createClient(client);
    }

    @Test
    void getClientReturnsOk() {
        Client client = ClientServiceTest.createClient();
        when(clientService.getClient(1L)).thenReturn(client);

        ResponseEntity<Client> response = clientController.getClient(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(client, response.getBody());
        verify(clientService).getClient(1L);
    }

    @Test
    void updateClientReturnsOk() {
        Client client = ClientServiceTest.createClient();
        when(clientService.updateClient(1L, client)).thenReturn(client);

        ResponseEntity<Client> response = clientController.updateClient(1L, client);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(client, response.getBody());
        verify(clientService).updateClient(1L, client);
    }

    @Test
    void deleteClientReturnsNoContent() {
        ResponseEntity<HttpStatus> response = clientController.deleteClient(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(clientService).deleteClient(1L);
    }
}
