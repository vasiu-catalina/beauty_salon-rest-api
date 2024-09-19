package com.vasiu_catalina.beauty_salon.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.vasiu_catalina.beauty_salon.entity.Client;
import com.vasiu_catalina.beauty_salon.exception.client.ClientAlreadyExistsException;
import com.vasiu_catalina.beauty_salon.exception.client.ClientNotFoundException;
import com.vasiu_catalina.beauty_salon.repository.ClientRepository;
import com.vasiu_catalina.beauty_salon.service.IClientService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ClientServiceImpl implements IClientService {

    private ClientRepository clientRepository;

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

        if (existsClientByEmail(client.getEmail()))
            throw new ClientAlreadyExistsException("Email");

        if (existsClientByPhoneNumber(client.getPhoneNumber()))
            throw new ClientAlreadyExistsException("Phone number");

        return clientRepository.save(client);
    }

    @Override
    public Client updateClient(Long id, Client client) {
        Client existingClient = this.getClient(id);

        if (!existingClient.getEmail().equals(client.getEmail())) {

            if (existsClientByEmail(client.getEmail()))
                throw new ClientAlreadyExistsException("Email");

            existingClient.setEmail(client.getEmail());
        }

        if (!existingClient.getEmail().equals(client.getEmail())) {

            if (existsClientByPhoneNumber(client.getPhoneNumber()))
                throw new ClientAlreadyExistsException("Phone number");

            existingClient.setPhoneNumber(client.getPhoneNumber());
        }

        existingClient.setFirstName(client.getFirstName());
        existingClient.setLastName(client.getLastName());
        existingClient.setBirthDate(client.getBirthDate());
        existingClient.setAddress(client.getAddress());

        return clientRepository.save(existingClient);
    }

    @Override
    public void deleteClient(Long id) {
        this.getClient(id);
        clientRepository.deleteById(id);
    }

    @Override
    public Client getClientByEmail(String email) {
        Optional<Client> client = clientRepository.findByEmail(email);
        return unwrappedClientWithPassword(client, null);
    }

    private boolean existsClientByEmail(String email) {
        Optional<Client> existingClientByEmail = clientRepository.findByEmail(email);
        return existingClientByEmail.isPresent();
    }

    private boolean existsClientByPhoneNumber(String phoneNumber) {
        Optional<Client> existingClientByPhoneNumber = clientRepository.findByPhoneNumber(phoneNumber);
        return existingClientByPhoneNumber.isPresent();
    }

    static Client unwrappedClient(Optional<Client> client, Long id) {
        if (client.isPresent()) {
            Client existing = client.get();
            existing.setPassword(null);
            return existing;
        }
        throw new ClientNotFoundException(id);
    }

    static Client unwrappedClientWithPassword(Optional<Client> client, Long id) {
        if (client.isPresent()) {
            return client.get();
        }
        throw new ClientNotFoundException(id);
    }

}
