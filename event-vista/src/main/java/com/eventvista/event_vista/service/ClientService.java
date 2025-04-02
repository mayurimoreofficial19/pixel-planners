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

    // Add a new client
    public Client addClient(Client client) {
        return clientRepository.save(client);
    }

    // Get all clients
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    // Search clients by name
    public List<Client> searchClientsByName(String name) {
        return clientRepository.findByNameContainingIgnoreCase(name);
    }

    // Delete a client by ID
    public void deleteClient(Integer id) {
        clientRepository.deleteById(id);
    }
}
