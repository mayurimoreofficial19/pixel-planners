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

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @NotNull(message = "Timezone is required for calendar synchronization")
    private String timezone;

    // Google Calendar specific fields
    private String googleCalendarId;
    private LocalDateTime lastSyncTime;
    private boolean syncEnabled = false;

    public Calendar() {
    }

    public Calendar(User user, String timezone) {
        this.user = user;
        this.timezone = timezone;
        this.name = user.getEmailAddress() + "'s Calendar";
    }

    // Getters and setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (this.name == null && user != null) {
            this.name = user.getEmailAddress() + "'s Calendar";
        }
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getGoogleCalendarId() {
        return googleCalendarId;
    }

    public void setGoogleCalendarId(String googleCalendarId) {
        this.googleCalendarId = googleCalendarId;
    }

    public LocalDateTime getLastSyncTime() {
        return lastSyncTime;
    }

    public void setLastSyncTime(LocalDateTime lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }

    public boolean isSyncEnabled() {
        return syncEnabled;
    }

    public void setSyncEnabled(boolean syncEnabled) {
        this.syncEnabled = syncEnabled;
    }

    public void addEvent(Event event) {
        events.add(event);
        event.setCalendar(this);
    }

    public void removeEvent(Event event) {
        events.remove(event);
        event.setCalendar(null);
    }

    @Override
    public String toString() {
        return name;
    }
}

