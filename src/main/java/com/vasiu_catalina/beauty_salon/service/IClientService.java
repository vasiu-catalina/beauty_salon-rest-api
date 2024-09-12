package com.vasiu_catalina.beauty_salon.service;

import java.util.List;

import com.vasiu_catalina.beauty_salon.entity.Client;

public interface IClientService {

    List<Client> getAllClients();

    Client getClient(Long id);

    Client createClient(Client client);

    Client updateClient(Long id, Client client);

    void deleteClient(Long id);
}
