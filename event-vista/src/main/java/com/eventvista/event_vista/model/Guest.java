package com.eventvista.event_vista.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Guest extends AbstractEntity{

    @Column(unique = true)
    @NotBlank(message = "Field must have valid guest name entered")
    @Size(min = 3, max = 100, message = "Field must be between 3 and 100 characters")
    private String name;

    @Column(unique = true)
    @NotBlank(message ="Field must have valid guest email entered")
    @Email(message = "Field must have valid email entered")
    private String emailAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "rsvp_status")
    private RSVPStatus rsvpStatus = RSVPStatus.PENDING;

    @Size(max = 500, message = "Field must be less than 500 characters")
    private String notes;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

//    @ManyToOne
//    @JoinColumn(name = "guest_list_id", nullable = false)
//    private GuestList guestList;

    public Guest() {
    }

    public Guest(String name, String emailAddress, String notes,RSVPStatus rsvpStatus) {
        this.name = name;
        this.emailAddress = emailAddress;
        this.notes = notes;
        this.rsvpStatus = rsvpStatus;
    }

    public Guest(String name, String emailAddress, String notes) {
        this(name, emailAddress, notes, RSVPStatus.PENDING);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public RSVPStatus getRsvpStatus() {
        return rsvpStatus;
    }

    public void setRsvpStatus(RSVPStatus rsvpStatus) {
        this.rsvpStatus = rsvpStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
