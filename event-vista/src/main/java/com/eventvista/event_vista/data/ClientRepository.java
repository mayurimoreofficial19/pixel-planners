package com.eventvista.event_vista.data;

import com.eventvista.event_vista.model.Client;
import com.eventvista.event_vista.model.User;
import com.eventvista.event_vista.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client,Integer> {
    List<Client> findByUser(User user);
}
