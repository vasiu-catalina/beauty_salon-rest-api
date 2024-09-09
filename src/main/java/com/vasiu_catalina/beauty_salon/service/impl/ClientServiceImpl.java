package com.vasiu_catalina.beauty_salon.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.vasiu_catalina.beauty_salon.entity.Client;
import com.vasiu_catalina.beauty_salon.exception.ClientAlreadyExistsException;
import com.vasiu_catalina.beauty_salon.exception.ClientNotFoundException;
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
        return clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException(id));
    }

    @Override
    public Client createClient(Client client) {
        if (existsClientByEmail(client.getEmail())) throw new ClientAlreadyExistsException("Email");
        if (existsClientByPhoneNumber(client.getPhoneNumber())) throw new ClientAlreadyExistsException("Phone number");
        return clientRepository.save(client);
    }

    @Override
    public Client updateClient(Long id, Client client) {
        Client existingClient = clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException(id));;

        if (!existingClient.getEmail().equals(client.getEmail())) {
            if (existsClientByEmail(client.getEmail())) throw new ClientAlreadyExistsException("Email");
            existingClient.setEmail(client.getEmail());
        }
        if (!existingClient.getEmail().equals(client.getEmail()))  {
            if (existsClientByPhoneNumber(client.getPhoneNumber())) throw new ClientAlreadyExistsException("Phone number");
            existingClient.setPhoneNumber(client.getPhoneNumber());
        }
        existingClient.setFirstName(client.getFirstName());
        existingClient.setLastName(client.getLastName());
        existingClient.setBirthDate(client.getBirthDate());

        return clientRepository.save(existingClient);
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
}
