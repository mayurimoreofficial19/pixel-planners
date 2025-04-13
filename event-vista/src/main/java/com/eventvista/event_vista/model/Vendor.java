package com.eventvista.event_vista.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.*;

@Entity
public class Vendor extends AbstractEntity implements Serializable {

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank(message = "Field must have valid vendor name entered")
    @Size(min = 3, max = 100, message = "Field must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Field must have valid vendor location entered")
    @Size(min = 3, max = 100, message = "Field must be between 3 and 100 characters")
    private String location;

    @ManyToMany
    private List<Skill> skills = new ArrayList<>();

    @NotNull(message ="Field must have valid venue phone number entered")
    @Embedded
    private PhoneNumber phoneNumber;

    @NotBlank(message ="Field must have valid vendor email entered")
    @Email(message = "Field must have valid email entered")
    private String emailAddress;

    @Size(max = 500, message = "Field must be less than 500 characters")
    private String notes;

    @ManyToMany(mappedBy = "vendors")
    @JsonIgnore
    private List<Event> events = new ArrayList<>();

    // Constructor
    public Vendor() {
    }

    public Vendor(String name, String location, List<Skill> skills, PhoneNumber phoneNumber, String emailAddress, String notes) {
        this.name = name;
        this.location = location;
        this.skills = skills;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.notes = notes;
    }

    // Getters and setters

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
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
