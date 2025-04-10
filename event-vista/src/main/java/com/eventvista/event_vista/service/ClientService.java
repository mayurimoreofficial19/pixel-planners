package com.eventvista.event_vista.service;

import com.eventvista.event_vista.data.ClientRepository;
import com.eventvista.event_vista.model.Client;
import com.eventvista.event_vista.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // Add new client
    public Client addClient(Client client) {
        return clientRepository.save(client);
    }

    // Get all clients
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    // Find by ID
    public Optional<Client> findById(Long id) {
        return clientRepository.findById(id);
    }

    // Search by Name (partial match)
    public List<Client> findByName(String name) {
        return clientRepository.findByNameContainingIgnoreCase(name);
    }

    // Find by exact Email
    public Optional<Client> findByEmail(String email) {
        return clientRepository.findByEmailIgnoreCase(email);
    }

    // Find by exact Phone Number
    public Optional<Client> findByPhoneNumber(String phoneNumber) {
        return clientRepository.findByPhoneNumber(phoneNumber);
    }
}
