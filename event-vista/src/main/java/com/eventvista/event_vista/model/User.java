package com.eventvista.event_vista.model;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
public class User extends AbstractEntity {
    @NotNull
    private String username;

    @NotNull
    private String pwHash;

    private String email;

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


}

