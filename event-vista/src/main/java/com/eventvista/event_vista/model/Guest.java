package com.eventvista.event_vista.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Guest extends AbstractEntity{

    @NotBlank(message = "Guest name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Column(name = "email_address")
    private String emailAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "rsvp_status")
    private RSVPStatus rsvpStatus = RSVPStatus.PENDING;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "guest_list_id", nullable = false)
    private GuestList guestList;

    public Guest() {}

    public Guest(String name, String emailAddress, GuestList guestList, String notes,RSVPStatus rsvpStatus) {
        this.name = name;
        this.emailAddress = emailAddress;
        this.guestList = guestList;
        this.notes = notes;
        this.rsvpStatus = rsvpStatus;
    }

    // Optional constructor with default RSVPStatus
    public Guest(String name, String emailAddress, GuestList guestList, String notes) {
        this(name, emailAddress, guestList, notes, RSVPStatus.PENDING);
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

    public GuestList getGuestList() {
        return guestList;
    }

    public void setGuestList(GuestList guestList) {
        this.guestList = guestList;
    }
}


