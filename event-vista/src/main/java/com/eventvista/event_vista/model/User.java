package com.eventvista.event_vista.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class User {
    @Id
    @GeneratedValue
    private int id;

    // Constructor
    public User() {
    }

    // Getters and setters
    public int getId() {
        return id;
    }



    // Override
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return id == that.getId();
    }


    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

