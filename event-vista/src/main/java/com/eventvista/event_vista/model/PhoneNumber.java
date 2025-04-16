package com.eventvista.event_vista.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

@Embeddable
public class PhoneNumber implements Serializable {

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^(\\(\\d{3}\\)\\s?|\\d{3}[-.]?)\\d{3}[-.]?\\d{4}$",
            message = "Please enter a valid phone number (e.g., (123) 456-7890, 123-456-7890, or 1234567890)")
    private String phoneNumber;

    public PhoneNumber() {
    }

    public PhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return phoneNumber;
    }
}
