package com.eventvista.event_vista.service;


import com.eventvista.event_vista.data.GuestRepository;
import com.eventvista.event_vista.model.Guest;
import com.eventvista.event_vista.model.GuestList;
import com.eventvista.event_vista.model.RSVPStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GuestService {
    private final GuestRepository guestRepository;

    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    // Find all guests by guest list
    public List<Guest> findAllGuestByGuestList(GuestList guestList) {
        return guestRepository.findByGuestList_Id(guestList.getId());
    }

    // Find guest by ID and guest list
    public Optional<Guest> findGuestById(Integer id,GuestList guestList) {
        return guestRepository.findById(id).filter(guest -> guest.getGuestList().getId().equals(guestList.getId()));
    }

    // Find guest by email and guest list
    public Optional<Guest> findGuestByEmail(String email,GuestList guestList) {
        return guestRepository.findByEmailAddressAndGuestList_Id(email,guestList.getId());
    }

    // Filter guests by RSVP status
    public List<Guest> findGuestByRSVPStatus(RSVPStatus status) {
        return guestRepository.findByRsvpStatus(status);
    }

    // Guests by guest list and RSVP status
    public List<Guest> findGuestByGuestListAndRSVP(GuestList guestList,RSVPStatus status) {
        return guestRepository.findByGuestList_IdAndRsvpStatus(guestList.getId(),status);
    }

    public List<Guest> findAllGuestsByGuestList(GuestList guestList) {
        return guestRepository.findByGuestList_Id(guestList.getId());
    }


    // Add a new guest
    public Guest addGuest(Guest guest,GuestList guestList) {
        guest.setGuestList(guestList);
        return guestRepository.save(guest);
    }

    // Update a guest
    public Optional<Guest> updateGuest(Integer id,Guest updatedGuest,GuestList guestList) {
        return guestRepository.findById(id)
                .filter(guest -> guest.getGuestList().getId().equals(guestList.getId()))
                .map(guest -> {
                    guest.setName(updatedGuest.getName());
                    guest.setEmailAddress(updatedGuest.getEmailAddress());
                    guest.setNotes(updatedGuest.getNotes());
                    guest.setRsvpStatus(updatedGuest.getRsvpStatus());
                    return guestRepository.save(guest);
                });
    }

    // Delete guest
    public boolean deleteGuest(Integer id,GuestList guestList) {
        return guestRepository.findById(id)
                .filter(guest -> guest.getGuestList().getId().equals(guestList.getId()))
                .map(guest -> {
                    guestRepository.delete(guest);
                    return true;
                }).orElse(false);
    }
}
