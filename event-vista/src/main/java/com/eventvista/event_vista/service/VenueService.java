package com.eventvista.event_vista.service;


import com.eventvista.event_vista.data.UserRepository;
import com.eventvista.event_vista.data.VenueRepository;
import com.eventvista.event_vista.model.User;
import com.eventvista.event_vista.model.Venue;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VenueService {

    private final VenueRepository venueRepository;


    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;

    }


    public List<Venue> getAllVenues(User user) {
        return venueRepository.findByUser(user);
    }

    public Optional<Venue> getVenueById(int venueId, User user) {
        return venueRepository.findById(venueId).filter(venue -> venue.getUser().getId().equals(user.getId()));
    }

    public Optional<Venue> getVenueByName(String name, User user) {
        return venueRepository.findByNameAndUser(name, user)
                .filter(venue -> venue.getUser().getId().equals(user.getId()));
    }

    public Optional<Venue> getVenueByLocation(String location, User user) {
        return venueRepository.findByLocationAndUser(location, user)
                .filter(venue -> venue.getUser().getId().equals(user.getId()));
    }



    public Venue createVenue(Venue venue, User user) {
        venue.setUser(user);
        return venueRepository.save(venue);
    }

    public Optional<Venue> updateVenue(int venueId, Venue updatedVenue, User user) {
        return venueRepository.findById(venueId)
                .filter(venue -> venue.getUser().getId().equals(user.getId()))
                .map(venue -> {
                    venue.setName(updatedVenue.getName());
                    venue.setCapacity(updatedVenue.getCapacity());
                    venue.setEmailAddress(updatedVenue.getEmailAddress());
                    venue.setLocation(updatedVenue.getLocation());
                    venue.setNotes(updatedVenue.getNotes());
                    venue.setPhoneNumber(updatedVenue.getPhoneNumber());
                    return venueRepository.save(venue);
                });
    }

        public boolean deleteVenue(int venueId, User user) {
            return venueRepository.findById(venueId)
                    .filter(venue -> venue.getUser().getId().equals(user.getId()))
                    .map(venue -> {
                        venueRepository.delete(venue);
                        return true;
                    })
                    .orElse(false);
        }
    }

