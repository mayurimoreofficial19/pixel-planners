package com.eventvista.event_vista.data;

import com.eventvista.event_vista.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client,Integer> {
    List<Client> findByNameContainingIgnoreCase(String name);
}
