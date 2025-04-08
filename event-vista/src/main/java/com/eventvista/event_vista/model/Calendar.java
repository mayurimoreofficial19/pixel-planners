package com.eventvista.event_vista.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Calendar extends AbstractEntity {

    @OneToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "Calendar must be associated with a user")
    private User user;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> events = new ArrayList<>();

    public Calendar() {
    }

    // Getters and setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void addEvent(Event event) {
        events.add(event);
        event.setCalendar(this);
    }
    public void removeEvent(Event event) {
        events.remove(event);
        event.setCalendar(null);
    }


}

