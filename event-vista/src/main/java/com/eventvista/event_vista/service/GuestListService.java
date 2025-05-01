package com.eventvista.event_vista.service;

import com.eventvista.event_vista.data.GuestListRepository;
import com.eventvista.event_vista.model.GuestList;
import com.eventvista.event_vista.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GuestListService {
    private final GuestListRepository guestListRepository;

    public GuestListService(GuestListRepository guestListRepository) {
        this.guestListRepository = guestListRepository;
    }

    // Find all guest lists for a user
    public List<GuestList> findAllByUser(User user) {
        return guestListRepository.findAllByEvent_User(user);
    }

    // Find a specific guest list by ID and user (used to scope access)
    public Optional<GuestList> findByIdAndUser(Integer id, User user) {
        return guestListRepository.findById(id)
                .filter(guestList -> guestList.getEvent().getUser().equals(user.getId()));
    }

    // Create new guest list
    public GuestList CreateGuestList(GuestList guestList,User user) {
        guestList.getEvent().setUser(user); // Ensure user owns the event
        return guestListRepository.save(guestList);
    }

    // Update guest list
    public Optional<GuestList> updateGuestList(Integer id,GuestList updatedGuestList,User user) {
        return guestListRepository.findById(id)
                .filter(guestList -> guestList.getEvent().getUser().equals(user.getId()))
                .map(existingList -> {
                    existingList.setName(updatedGuestList.getName());
                    //can update more fields here if needed
                    return guestListRepository.save(existingList);
                });
    }

    //Delete guest list
    public boolean deleteGuestList(Integer id,User user) {
        return guestListRepository.findById(id)
                .filter(guestList -> guestList.getEvent().getUser().getId().equals(user.getId()))
                .map(guestList -> {
                    guestListRepository.delete(guestList);
                    return(true);
                }).orElse(false);
    }
}
