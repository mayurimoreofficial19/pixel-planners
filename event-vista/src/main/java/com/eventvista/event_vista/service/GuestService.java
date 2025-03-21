package com.eventvista.event_vista.service;


import com.eventvista.event_vista.data.GuestRepository;
import com.eventvista.event_vista.model.Guest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GuestService {
    @Autowired
    private GuestRepository guestRepository;

    public Guest addGuest(Guest guest) {
        return guestRepository.save(guest);
    }

    public List<Guest> getAllGuests() {
        return (List<Guest>) guestRepository.findAll();
    }

    public boolean updateRsvp(String emailAddress, boolean rsvpStatus) {
        Optional<Guest> guestOpt = guestRepository.findByEmailAddress(emailAddress);
        if (guestOpt.isPresent()) {
            Guest guest = guestOpt.get();
            guest.setRsvp(rsvpStatus);
            guestRepository.save(guest);
            return true;
        }
        return false;
    }

    public boolean removeGuest(String emailAddress) {
        Optional<Guest> guestOpt = guestRepository.findByEmailAddress(emailAddress);
        if (guestOpt.isPresent()) {
            guestRepository.delete(guestOpt.get());
            return true;
        }
        return false;
    }
}
