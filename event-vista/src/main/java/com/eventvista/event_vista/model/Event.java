package com.eventvista.event_vista.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Event {
    @Id
    @GeneratedValue
    private String name;
    private String date;
    private String time;
    private String notes;

    public Event() {

    }
}
