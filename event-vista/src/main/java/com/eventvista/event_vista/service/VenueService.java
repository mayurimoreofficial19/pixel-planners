package com.eventvista.event_vista.service;


import com.eventvista.event_vista.data.UserRepository;
import com.eventvista.event_vista.data.VenueRepository;
import com.eventvista.event_vista.exception.VendorNotFoundException;
import com.eventvista.event_vista.model.PhoneNumber;
import com.eventvista.event_vista.model.User;
import com.eventvista.event_vista.model.Vendor;
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


    public List<Venue> findAllVenues(User user) {
        return venueRepository.findAllByUser(user);
    }


    public Optional<Venue> findVenueById(Integer id, User user) {
        return venueRepository.findVenueById(id, user)
                .filter(venue -> venue.getUser().getId().equals(user.getId()));
    }

    public Optional<Venue> findVenueByName(String name, User user) {
        return venueRepository.findVenueByName(name, user)
                .filter(venue -> venue.getUser().getId().equals(user.getId()));
    }

    public Optional<Venue> findVenueByLocation(String location, User user) {
        return venueRepository.findVenueByLocation(location, user)
                .filter(venue -> venue.getUser().getId().equals(user.getId()));
    }

    public Optional<Venue> findVenueByEmailAddress(String emailAddress, User user) {
        return venueRepository.findVenueByEmailAddress(emailAddress, user)
                .filter(venue -> venue.getUser().getId().equals(user.getId()));
    }

    public Optional<Venue> findVenueByPhoneNumber(PhoneNumber phoneNumber, User user) {
        return venueRepository.findVenueByPhoneNumber(phoneNumber, user)
                .filter(venue -> venue.getUser().getId().equals(user.getId()));
    }

    public Venue addVenue(Venue venue, User user) {
        venue.setUser(user);
        return venueRepository.save(venue);
    }


//
//    public Venue createVenue(Venue venue, User user) {
//        venue.setUser(user);
//        return venueRepository.save(venue);
//    }

    public Optional<Venue> updateVenue(Integer id, Venue updatedVenue, User user) {
        return venueRepository.findVenueById(id, user)
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

    public boolean deleteVenue(Integer id, User user) {
        Optional<Venue> venueOpt = venueRepository.findVenueById(id, user);

        if (venueOpt.isPresent()) {
            venueRepository.delete(venueOpt.get()); // Delete the venue if it exists and belongs to the user
            return true; // Shows the venue was successfully deleted
        }
        return false; // Return false if the venue doesn't exist
    }
}

