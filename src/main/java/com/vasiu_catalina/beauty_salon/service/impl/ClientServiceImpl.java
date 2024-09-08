package com.vasiu_catalina.beauty_salon.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.vasiu_catalina.beauty_salon.entity.Client;
import com.vasiu_catalina.beauty_salon.repository.ClientRepository;
import com.vasiu_catalina.beauty_salon.service.ClientService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ClientServiceImpl implements ClientService {

    ClientRepository clientRepository;

    @Override
    public List<Client> getAllClients() {
        return (List<Client>) clientRepository.findAll();
    }

    @Override
    public Client getClient(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        return unwrappedClient(client, id);
    }

    @Override
    public Client createClient(Client client) {
        if (existsClientByEmail(client.getEmail())) new RuntimeException("Client with email " + client.getEmail() + " already exists.");
        if (existsClientByPhoneNumber(client.getPhoneNumber())) new RuntimeException("Client with phone number " + client.getPhoneNumber() + " already exists.");
        return clientRepository.save(client);
    }

    @Override
    public Client updateClient(Long id, Client client) {
        Optional<Client> existingClient = clientRepository.findById(id);
        Client unwrappedExistingClient = unwrappedClient(existingClient, id);

        if (!unwrappedExistingClient.getEmail().equals(client.getEmail())) {
            if (existsClientByEmail(client.getEmail())) new RuntimeException("Client with email " + client.getEmail() + " already exists.");
            unwrappedExistingClient.setEmail(client.getEmail());
        }
        if (!unwrappedExistingClient.getEmail().equals(client.getEmail()))  {
            if (existsClientByPhoneNumber(client.getPhoneNumber())) new RuntimeException("Client with phone number " + client.getPhoneNumber() + " already exists.");
            unwrappedExistingClient.setPhoneNumber(client.getPhoneNumber());
        }
        unwrappedExistingClient.setFirstName(client.getFirstName());
        unwrappedExistingClient.setLastName(client.getLastName());
        unwrappedExistingClient.setBirthDate(client.getBirthDate());

        return clientRepository.save(unwrappedExistingClient);
    }

    @Override
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    private boolean existsClientByEmail(String email) {
        Optional<Client> existingClientByEmail = clientRepository.findByEmail(email);
        return existingClientByEmail.isPresent();
    }

    public boolean existsClientByPhoneNumber(String phoneNumber) {
        Optional<Client> existingClientByPhoneNumber = clientRepository.findByPhoneNumber(phoneNumber);
        return existingClientByPhoneNumber.isPresent();
    }
    
    static Client unwrappedClient(Optional<Client> client, Long id) {
        if (client.isPresent())
            return client.get();
        throw new RuntimeException("Client with id " + id + " was not found.");
    }
}
