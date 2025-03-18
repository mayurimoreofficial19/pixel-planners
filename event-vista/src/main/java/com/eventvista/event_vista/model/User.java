package com.eventvista.event_vista.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User extends AbstractEntity {
    @NotNull
    private String username;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String pwHash;

    @OneToMany(mappedBy = "user")
    private List<Event> events = new ArrayList<>();

    // Static method to use the bcrypt dependency for encoding
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public User() {}

//    public User(String username, String password) {
//        this.username = username;
//        this.pwHash = encoder.encode(password);
//    }

    public User(String username, String pwHash, String email) {
        this.username = username;
        this.pwHash = encoder.encode(pwHash);
        this.email = email;
    }

    public String getPwHash() {
        return pwHash;
    }

    public void setPwHash(String pwHash) {
        this.pwHash = encoder.encode(pwHash);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Instance method to use the bcrypt multi-step matcher (.equals is not enough)
    public boolean isMatchingPassword(String password) {
        return encoder.matches(password, pwHash);
    }


    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}

