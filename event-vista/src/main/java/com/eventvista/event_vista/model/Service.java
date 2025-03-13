package com.eventvista.event_vista.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Service {

    @Id
    @GeneratedValue
    private int id;

    @NotBlank(message = "Field must have valid service or skill name entered")
    @Size(min = 3, max = 100, message = "Field must be between 3 and 100 characters")
    private String name;

    @ManyToMany(mappedBy = "service")
    private Set<Vendor> vendor = new HashSet<>();

    // Constructor
    public Service() {
    }

    public Service(String name) {
        this.name = name;
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

    public Set<Vendor> getVendor() {
        return vendor;
    }

    public void setVendor(Set<Vendor> vendor) {
        this.vendor = vendor;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service that = (Service) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
