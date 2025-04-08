package com.eventvista.event_vista.data;

import com.eventvista.event_vista.model.Event;
import com.eventvista.event_vista.model.User;
import com.eventvista.event_vista.model.Venue;
import com.eventvista.event_vista.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    List<Event> findAllByUser(User user);

    Optional<Event> findByIdAndUser(Integer id, User user);

    Optional<Event> findByNameAndUser(String name, User user);

    //To find past, upcoming, and dates within a specific range
    List<Event> findByDateBetweenAndUser(LocalDate startDate, LocalDate endDate, User user);

    List<Event> findByVenueIdAndUser(Integer venueId, User user);

    List<Event> findByClientIdAndUser(Integer clientId, User user);

    List<Event> findByVendorsIdAndUser(Integer vendorId, User user);

}

