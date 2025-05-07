package com.eventvista.event_vista.service;

import com.eventvista.event_vista.data.ClientRepository;
import com.eventvista.event_vista.data.GuestRepository;
import com.eventvista.event_vista.model.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GuestService {
    private final GuestRepository guestRepository;


    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;

    }

    public List<Guest> findAllGuests(User user) {
        return guestRepository.findAllByUser(user);
    }


    public Optional<Guest> findGuestById(Integer id, User user) {
        return guestRepository.findByIdAndUser(id, user)
                .filter(guest -> guest.getUser().getId().equals(user.getId()));
    }

    public Optional<Guest> findGuestByName(String name, User user) {
        return guestRepository.findByNameAndUser(name, user)
                .filter(guest -> guest.getUser().getId().equals(user.getId()));
    }

    public Optional<Guest> findGuestByEmailAddress(String emailAddress, User user) {
        return guestRepository.findByEmailAddressAndUser(emailAddress, user)
                .filter(guest -> guest.getUser().getId().equals(user.getId()));
    }

    public List<Guest> findGuestsByRsvpStatus(String status, User user) {
        try {
            RSVPStatus rsvpStatus = RSVPStatus.valueOf(status);
            return guestRepository.findByRsvpStatusAndUser(rsvpStatus, user);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid RSVP status: " + status);
        }
    }



    public Guest addGuest(Guest guest, User user) {
        guest.setUser(user);
        return guestRepository.save(guest);
    }

    public Optional<Guest> updateGuest(Integer id, Guest updatedGuest, User user) {
        return guestRepository.findByIdAndUser(id, user)
                .map(guest -> {

                    guest.setName(updatedGuest.getName());
                    guest.setEmailAddress(updatedGuest.getEmailAddress());
                    guest.setRsvpStatus(updatedGuest.getRsvpStatus());
                    guest.setNotes(updatedGuest.getNotes());

                    return guestRepository.save(guest);
                });
    }

    public boolean deleteGuest(Integer id, User user) {
        Optional<Guest> guestOpt = guestRepository.findByIdAndUser(id, user);

        if (guestOpt.isPresent()) {
            guestRepository.delete(guestOpt.get());
            return true;
        }
        return false;
    }
}
