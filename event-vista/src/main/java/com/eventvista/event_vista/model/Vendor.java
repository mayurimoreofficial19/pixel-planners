package com.eventvista.event_vista.model;

import jakarta.persistence.ManyToMany;

public class Vendor {

    @ManyToMany
    Service service;
}
