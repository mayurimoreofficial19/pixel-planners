package com.eventvista.event_vista.data;

import com.eventvista.event_vista.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

}
