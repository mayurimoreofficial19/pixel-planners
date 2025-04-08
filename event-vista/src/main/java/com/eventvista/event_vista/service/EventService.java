package com.eventvista.event_vista.service;

import com.eventvista.event_vista.data.EventRepository;
import com.eventvista.event_vista.model.Event;
import com.eventvista.event_vista.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final VenueService venueService;
    private final CalendarService calendarService;

    public EventService(EventRepository eventRepository, VenueService venueService, CalendarService calendarService) {
        this.eventRepository = eventRepository;
        this.venueService = venueService;
        this.calendarService = calendarService;
    }

    public List<Event> findAllEvents(User user) {
        return eventRepository.findAllByUser(user);
    }

    public Optional<Event> findEventById(Integer id, User user) {
        return eventRepository.findByIdAndUser(id, user);
    }

    public Optional<Event> findEventByName(String name, User user) {
        return eventRepository.findByNameAndUser(name, user);
    }

    public List<Event> findEventsByVenue(Integer venueId, User user) {
        return eventRepository.findByVenueIdAndUser(venueId, user);
    }

    public List<Event> findEventsByClient(Integer clientId, User user) {
        return eventRepository.findByClientIdAndUser(clientId, user);
    }

    public List<Event> findEventsByVendor(Integer vendorId, User user) {
        return eventRepository.findByVendorsIdAndUser(vendorId, user);
    }

    //Will need to add Vendor, Client, GuestList later
    public Event addEvent(Event event, User user) {
        // Set the user
        event.setUser(user);

        // Handle venue relationship
        if (event.getVenue() != null && event.getVenue().getId() != null) {
            venueService.findVenueById(event.getVenue().getId(), user)
                    .ifPresent(event::setVenue);
        }

        // Set the user's calendar automatically
        calendarService.findCalendarByUser(user)
                .ifPresent(event::setCalendar);

        return eventRepository.save(event);
    }

    public Event updateEvent(Integer id, Event updatedEvent, User user) {
        return eventRepository.findById(id)
                .map(existingEvent -> {
                    // Update basic fields
                    existingEvent.setName(updatedEvent.getName());
                    existingEvent.setDate(updatedEvent.getDate());
                    existingEvent.setTime(updatedEvent.getTime());
                    existingEvent.setNotes(updatedEvent.getNotes());

                    // Handle venue relationship
                    if (updatedEvent.getVenue() != null && updatedEvent.getVenue().getId() != null) {
                        venueService.findVenueById(updatedEvent.getVenue().getId(), user)
                                .ifPresent(existingEvent::setVenue);
                    }

                    // Calendar relationship remains unchanged as it's tied to the user
                    return eventRepository.save(existingEvent);
                })
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public boolean deleteEvent(Integer id, User user) {
        Optional<Event> event = eventRepository.findByIdAndUser(id, user);
        if (event.isPresent()) {
            eventRepository.delete(event.get());
            return true;
        }
        return false;
    }

    public Optional<Event> rebookEvent(Integer id, Event newEventDetails, User user) {
        return eventRepository.findByIdAndUser(id, user).map(originalEvent -> {
            //Create new event with all original data
            Event newEvent = new Event();

            //Copy all basic fields from original event
            newEvent.setName(originalEvent.getName());
            newEvent.setDate(originalEvent.getDate());
            newEvent.setTime(originalEvent.getTime());
            newEvent.setNotes(originalEvent.getNotes());

            //Copy all relationships from original event
            newEvent.setVenue(originalEvent.getVenue());
            newEvent.setVendors(originalEvent.getVendors());
            newEvent.setClient(originalEvent.getClient());
            newEvent.setCalendar(originalEvent.getCalendar());
            newEvent.setUser(user);

            //Update with new values if provided
            if (newEventDetails.getName() != null) {
                newEvent.setName(newEventDetails.getName());
            }

            //Date and time must be changed for rebooking
            if (newEventDetails.getDate() == null || newEventDetails.getTime() == null) {
                throw new RuntimeException("Date and time must be provided for rebooking");
            }
            newEvent.setDate(newEventDetails.getDate());
            newEvent.setTime(newEventDetails.getTime());

            if (newEventDetails.getNotes() != null) {
                newEvent.setNotes(newEventDetails.getNotes());
            }

            //Update relationships if new values are provided
            if (newEventDetails.getVenue() != null) {
                venueService.findVenueById(newEventDetails.getVenue().getId(), user)
                        .ifPresent(newEvent::setVenue);
            }

            if (newEventDetails.getVendors() != null && !newEventDetails.getVendors().isEmpty()) {
                newEvent.setVendors(newEventDetails.getVendors());
            }

            if (newEventDetails.getClient() != null) {
                newEvent.setClient(newEventDetails.getClient());
            }

            return eventRepository.save(newEvent);
        });
    }


}
