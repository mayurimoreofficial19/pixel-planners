package com.eventvista.event_vista.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Vendor extends AbstractEntity implements Serializable {

    @NotBlank(message = "Field must have valid vendor name entered")
    @Size(min = 3, max = 100, message = "Field must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Field must have valid vendor location entered")
    @Size(min = 3, max = 100, message = "Field must be between 3 and 100 characters")
    private String location;

    @ManyToMany
    private Set<Skill> skills = new HashSet<>();

    @NotBlank(message ="Field must have valid vendor phone number entered")
    @Embedded
    private PhoneNumber phoneNumber;

    @NotBlank(message ="Field must have valid vendor email entered")
    @Email(message = "Field must have valid email entered")
    private String emailAddress;

    @Size(max = 500, message = "Field must be less than 500 characters")
    private String notes;

    @ManyToMany(mappedBy = "vendors")
    private List<Event> events = new ArrayList<>();

    // Constructor
    public Vendor() {
    }

    public Vendor(String name, String location, Set<Skill> skills, PhoneNumber phoneNumber, String emailAddress, String notes) {
        this.name = name;
        this.location = location;
        this.skills = skills;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.notes = notes;
    }

    // Getters and setters

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

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
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

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return name;
    }


}
