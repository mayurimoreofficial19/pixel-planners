package com.eventvista.event_vista.controller;

import com.eventvista.event_vista.data.*;
import com.eventvista.event_vista.model.Event;
import com.eventvista.event_vista.model.User;
import com.eventvista.event_vista.model.Venue;
import com.eventvista.event_vista.model.Client;
import com.eventvista.event_vista.security.CustomUserPrincipal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:3000")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    // Get all events for the current user
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        try {
            Iterable<Event> events = eventRepository.findAll();
            List<Event> eventList = StreamSupport.stream(events.spliterator(), false)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(eventList);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Get a single event by ID
    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEvent(@PathVariable int eventId) {
        try {
            return eventRepository.findById(eventId)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Create a new event
    @PostMapping
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Event event) {
        try {
            // Get the authenticated user from the security context
            CustomUserPrincipal userPrincipal = (CustomUserPrincipal) SecurityContextHolder
                    .getContext().getAuthentication().getPrincipal();
            User user = userPrincipal.getUser();
            event.setUser(user);

            // Validate and save the event
            Event savedEvent = eventRepository.save(event);
            return ResponseEntity.ok(savedEvent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Update an existing event
    @PutMapping("/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable int eventId, @Valid @RequestBody Event updatedEvent) {
        try {
            return eventRepository.findById(eventId)
                    .map(event -> {
                        event.setName(updatedEvent.getName());
                        event.setDate(updatedEvent.getDate());
                        event.setTime(updatedEvent.getTime());
                        event.setNotes(updatedEvent.getNotes());
                        event.setVenue(updatedEvent.getVenue());
                        event.setVendors(updatedEvent.getVendors());
                        event.setClient(updatedEvent.getClient());
                        event.setCalendar(updatedEvent.getCalendar());
                        return ResponseEntity.ok(eventRepository.save(event));
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Delete an event
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable int eventId) {
        try {
            if (eventRepository.existsById(eventId)) {
                eventRepository.deleteById(eventId);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Get past events
    @GetMapping("/past")
    public ResponseEntity<List<Event>> getPastEvents() {
        try {
            Iterable<Event> events = eventRepository.findAll();
            List<Event> pastEvents = StreamSupport.stream(events.spliterator(), false)
                    .filter(event -> event.getDate().isBefore(LocalDate.now()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(pastEvents);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Get upcoming events
    @GetMapping("/upcoming")
    public ResponseEntity<List<Event>> getUpcomingEvents() {
        try {
            Iterable<Event> events = eventRepository.findAll();
            List<Event> upcomingEvents = StreamSupport.stream(events.spliterator(), false)
                    .filter(event -> event.getDate().isAfter(LocalDate.now()) || event.getDate().isEqual(LocalDate.now()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(upcomingEvents);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Rebook an event
    @PostMapping("/{eventId}/rebook")
    public ResponseEntity<Event> rebookEvent(@PathVariable int eventId, @Valid @RequestBody Event rebookedEvent) {
        try {
            return eventRepository.findById(eventId)
                    .map(originalEvent -> {
                        rebookedEvent.setUser(originalEvent.getUser());
                        return ResponseEntity.ok(eventRepository.save(rebookedEvent));
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/venue/{venueId}")
    public ResponseEntity<List<Event>> getEventsByVenue(@PathVariable int venueId) {
        try {
            Optional<Venue> venue = venueRepository.findById(venueId);
            if (venue.isPresent()) {
                Iterable<Event> events = eventRepository.findByVenue(venue.get());
                List<Event> eventList = StreamSupport.stream(events.spliterator(), false)
                        .collect(Collectors.toList());
                return ResponseEntity.ok(eventList);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Event>> getEventsByClient(@PathVariable int clientId) {
        try {
            Optional<Client> client = clientRepository.findById(clientId);
            if (client.isPresent()) {
                Iterable<Event> events = eventRepository.findByClient(client.get());
                List<Event> eventList = StreamSupport.stream(events.spliterator(), false)
                        .collect(Collectors.toList());
                return ResponseEntity.ok(eventList);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}


