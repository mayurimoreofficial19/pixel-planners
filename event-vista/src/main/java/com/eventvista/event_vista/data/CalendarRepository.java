package com.eventvista.event_vista.data;

import com.eventvista.event_vista.model.Calendar;
import com.eventvista.event_vista.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Integer> {
    Optional<Calendar> findByUser(User user);
}
