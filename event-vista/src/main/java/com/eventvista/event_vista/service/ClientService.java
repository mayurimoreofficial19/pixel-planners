package com.eventvista.event_vista.service;

import com.eventvista.event_vista.data.ClientRepository;
import com.eventvista.event_vista.model.Client;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client addClient(Client client) {
        return clientRepository.save(client);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public List<Client> searchClientsByName(String name) {
        return clientRepository.findByNameContainingIgnoreCase(name);
    }

    public void deleteClient(Integer id) {
        clientRepository.deleteById(id);
    }
}
