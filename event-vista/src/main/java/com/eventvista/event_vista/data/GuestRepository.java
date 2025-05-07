package com.eventvista.event_vista.data;

import com.eventvista.event_vista.model.Client;
import com.eventvista.event_vista.model.Guest;
import com.eventvista.event_vista.model.RSVPStatus;
import com.eventvista.event_vista.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest,Integer> {
    List<Guest> findAllByUser(User user);

    Optional<Guest> findByIdAndUser(Integer id, User user);

    List<Guest> findByRsvpStatusAndUser(RSVPStatus status, User user);

    Optional<Guest> findByNameAndUser(String name, User user);

    Optional<Guest> findByEmailAddressAndUser(String emailAddress, User user);
}
