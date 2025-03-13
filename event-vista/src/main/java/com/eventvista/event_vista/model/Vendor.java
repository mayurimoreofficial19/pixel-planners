package com.eventvista.event_vista.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Vendor {

    @Id
    @GeneratedValue
    private int id;

    @NotBlank(message = "Field must have valid vendor name entered")
    @Size(min = 3, max = 100, message = "Field must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Field must have valid vendor location entered")
    @Size(min = 3, max = 100, message = "Field must be between 3 and 100 characters")
    private String location;

    @ManyToMany
    private Set<Service> services = new HashSet<>();

    @NotBlank(message ="Field must have valid vendor phone number entered")
    private PhoneNumber phoneNumber;

    @NotBlank(message ="Field must have valid vendor email entered")
    @Email(message = "Field must have valid email entered")
    private String emailAddress;

    @Size(max = 500, message = "Field must be less than 500 characters")
    private String notes;

    @ManyToMany(mappedBy = "vendors")
    private Set<Event> events = new HashSet<>();

    // Constructor
    public Vendor() {
    }

    public Vendor(String name, String location, Set<Service> services, PhoneNumber phoneNumber, String emailAddress, String notes) {
        this.name = name;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.notes = notes;
    }
    // Getters and setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<Service> getServices() {
        return services;
    }

    public void setServices(Set<Service> services) {
        this.services = services;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vendor that = (Vendor) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
