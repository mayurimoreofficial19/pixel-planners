package com.eventvista.event_vista.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
public class Client extends AbstractEntity {

    @NotBlank(message = "Client name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message ="Field must have valid client phone number entered")
    private String phoneNumberInput;
    private PhoneNumber phoneNumber;

    private String notes;

    @OneToMany(mappedBy = "client")
    private List<Event> events = new ArrayList<>();

    public Client() {
    }

    public Client(String name, String email, String phoneNumberInput, String notes) {
        this.name = name;
        this.email = email;
        this.phoneNumberInput = phoneNumberInput;
        this.phoneNumber = new PhoneNumber(phoneNumberInput); // Initialize PhoneNumber
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumberInput() {
        return phoneNumberInput;
    }

    public void setPhoneNumberInput(String phoneNumberInput) {
        this.phoneNumberInput = phoneNumberInput;
        this.phoneNumber = new PhoneNumber(phoneNumberInput); // Reinitialize when set
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
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
