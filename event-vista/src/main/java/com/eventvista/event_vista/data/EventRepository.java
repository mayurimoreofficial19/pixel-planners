package com.eventvista.event_vista.data;

import com.eventvista.event_vista.model.Event;
import com.eventvista.event_vista.model.Venue;
import com.eventvista.event_vista.model.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends CrudRepository<Event, Integer> {
    Iterable<Event> findByVenue(Venue venue);
    Iterable<Event> findByClient(Client client);
}

