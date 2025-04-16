package com.eventvista.event_vista.controller;

import com.eventvista.event_vista.data.ClientRepository;
import com.eventvista.event_vista.data.UserRepository;
import com.eventvista.event_vista.model.Client;
import com.eventvista.event_vista.model.User;
import com.eventvista.event_vista.security.CustomUserPrincipal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/client")
@CrossOrigin(origins = "http://localhost:3000")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    private User getUserFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail;

        if (authentication.getPrincipal() instanceof CustomUserPrincipal) {
            CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUser();
        } else if (authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            userEmail = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
            return userRepository.findByEmailAddress(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } else {
            throw new RuntimeException("Unexpected principal type: " + authentication.getPrincipal().getClass());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllClients() {
        try {
            System.out.println("Getting all clients...");
            User user = getUserFromAuthentication();
            System.out.println("User found: " + user.getEmailAddress());
            List<Client> clients = clientRepository.findByUser(user);
            System.out.println("Found " + clients.size() + " clients");
            return ResponseEntity.ok(clients);
        } catch (Exception e) {
            System.err.println("Error in getAllclients: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error fetching client: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<?> getClient(@PathVariable int clientId) {
        try {
            User user = getUserFromAuthentication();

            return clientRepository.findById(clientId)
                    .map(client -> {
                        if (client.getUser().getId().equals(user.getId())) {
                            return ResponseEntity.ok(client);
                        }
                        return ResponseEntity.status(403).body(Map.of("error", "You do not have permission to access this client"));
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error fetching client: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping
    public ResponseEntity<?> createClient(@Valid @RequestBody Client client, BindingResult bindingResult) {
        try {
            System.out.println("Creating new client...");
            System.out.println("Client data received: " + client);

            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error -> {
                    System.err.println("Validation error - Field: " + error.getField() + ", Message: " + error.getDefaultMessage());
                    errors.put(error.getField(), error.getDefaultMessage());
                });
                return ResponseEntity.badRequest().body(errors);
            }

            User user = getUserFromAuthentication();
            System.out.println("User found: " + user.getEmailAddress());

            client.setUser(user);
            Client savedClient = clientRepository.save(client);
            System.out.println("Client saved successfully with ID: " + savedClient.getId());
            return ResponseEntity.ok(savedClient);
        } catch (Exception e) {
            System.err.println("Error in createClient: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error creating client: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{ClientId}")
    public ResponseEntity<?> updateClient(@PathVariable int clientId, @Valid @RequestBody Client updatedClient, BindingResult bindingResult) {
        try {
            System.out.println("Updating client with ID: " + clientId);
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error -> {
                    System.err.println("Validation error - Field: " + error.getField() + ", Message: " + error.getDefaultMessage());
                    errors.put(error.getField(), error.getDefaultMessage());
                });
                return ResponseEntity.badRequest().body(errors);
            }

            User user = getUserFromAuthentication();
            System.out.println("User found: " + user.getEmailAddress());

            return clientRepository.findById(clientId)
                    .map(client -> {
                        if (!client.getUser().getId().equals(user.getId())) {
                            return ResponseEntity.status(403).body(Map.of("error", "You do not have permission to update this client"));
                        }
                        client.setName(updatedClient.getName());
                        client.setEmailAddress(updatedClient.getEmailAddress());
                        client.setNotes(updatedClient.getNotes());
                        client.setPhoneNumber(updatedClient.getPhoneNumber());
                        Client saved = clientRepository.save(client);
                        System.out.println("Client updated successfully");
                        return ResponseEntity.ok(saved);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            System.err.println("Error in updateClient: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error updating client: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<?> deleteClient(@PathVariable int clientId) {
        try {
            System.out.println("Deleting client with ID: " + clientId);
            User user = getUserFromAuthentication();
            System.out.println("User found: " + user.getEmailAddress());

            return clientRepository.findById(clientId)
                    .map(client -> {
                        if (!client.getUser().getId().equals(user.getId())) {
                            return ResponseEntity.status(403).body(Map.of("error", "You do not have permission to delete this client"));
                        }
                        clientRepository.delete(client);
                        System.out.println("Client deleted successfully");
                        return ResponseEntity.ok().build();
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            System.err.println("Error in deleteClient: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error deleting client: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
