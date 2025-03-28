package com.eventvista.event_vista.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Event extends AbstractEntity{

    @ManyToOne
    private User user;

    @NotBlank(message = "Field must have valid event name entered")
    @Size(min = 3, max = 100, message = "Field must be between 3 and 100 characters")
    private String name;

    @NotNull(message = "Field must have valid event date entered")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull(message = "Field must have valid event time entered")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime time;

    @Size(max = 500, message = "Field must be less than 500 characters")
    private String notes;

    @ManyToOne
    private Venue venue;

    @ManyToMany
    private List<Vendor> vendors = new ArrayList<>();

    @ManyToOne
    private Client client;

    // Constructor

    public Event() {
    }

    public Event(String name, LocalDate date, LocalTime time, String notes, Venue venue, List<Vendor> vendors, Client client) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.notes = notes;
        this.venue = venue;
        this.vendors = vendors;
        this.client = client;
    }


    // Getters and setters


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public List<Vendor> getVendors() {
        return vendors;
    }

    public void setVendors(List<Vendor> vendors) {
        this.vendors = vendors;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return name;
    }

}
