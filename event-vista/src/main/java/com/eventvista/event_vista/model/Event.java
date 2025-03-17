package com.eventvista.event_vista.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Event extends AbstractEntity{

    @OneToMany
    private User user;

    @NotBlank(message = "Field must have valid event name entered")
    @Size(min = 3, max = 100, message = "Field must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Field must have valid event date entered")
    @DateTimeFormat
    private String date;

    @NotBlank(message = "Field must have valid event time entered")
    @DateTimeFormat
    private String time;

    @Size(max = 500, message = "Field must be less than 500 characters")
    private String notes;

    @ManyToOne
    private Venue venue;

    @ManyToMany
    private Set<Vendor> vendors = new HashSet<>();

    @ManyToOne
    private Client client;

    // Constructor
    public Event() {
    }

    public Event(String name, String date, String time, String notes, Venue venue, Set<Vendor> vendors, Client client) {
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Venue getVenue() {
        return venue;
    }

    public Set<Vendor> getVendors() {
        return vendors;
    }

    public void setVendors(Set<Vendor> vendors) {
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
