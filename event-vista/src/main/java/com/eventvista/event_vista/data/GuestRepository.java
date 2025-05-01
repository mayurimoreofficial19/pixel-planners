package com.eventvista.event_vista.data;

import com.eventvista.event_vista.model.Guest;
import com.eventvista.event_vista.model.RSVPStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Integer> {
    // Find by email address
    Optional<Guest> findByEmailAddress(String emailAddress);

    // Find all guests in a specific guest list
    List<Guest> findByGuestList_Id(Integer guestListId);

    // Find all guests with a specific RSVP status
    List<Guest> findByRsvpStatus(RSVPStatus rsvpStatus);

    // Find guests by guest list and RSVP status
    List<Guest> findByGuestList_IdAndRsvpStatus(Integer guestListId, RSVPStatus rsvpStatus);

    // Find a guest by email and guest list (helps check for duplicates)
    Optional<Guest> findByEmailAddressAndGuestList_Id(String emailAddress, Integer guestListId);
}
