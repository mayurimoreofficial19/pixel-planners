package com.eventvista.event_vista.data;

import com.eventvista.event_vista.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VenueRepository extends JpaRepository<Venue, Integer> {

}