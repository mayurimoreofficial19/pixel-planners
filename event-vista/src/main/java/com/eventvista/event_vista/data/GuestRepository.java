package com.eventvista.event_vista.data;

import com.eventvista.event_vista.model.Guest;
import org.springframework.data.repository.CrudRepository;

public interface GuestRepository extends CrudRepository<Guest, Integer> {

}
