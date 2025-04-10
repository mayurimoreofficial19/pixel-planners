package com.eventvista.event_vista.controller;

import com.eventvista.event_vista.model.Event;
import com.eventvista.event_vista.model.User;
import com.eventvista.event_vista.service.EventService;
import com.eventvista.event_vista.utilities.AuthUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:3000")
public class EventController {

    private final EventService eventService;
    private final AuthUtil authUtil;

    public EventController(EventService eventService, AuthUtil authUtil) {
        this.eventService = eventService;
        this.authUtil = authUtil;
    }

    // Get all events for the current user
    @GetMapping("/all")
    public ResponseEntity<List<Event>> getAllEvents() {
        User user = authUtil.getUserFromAuthentication();
        return ResponseEntity.ok(eventService.findAllEvents(user));
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable("id") Integer id) {
        User user = authUtil.getUserFromAuthentication();
        return ResponseEntity.of(eventService.findEventById(id, user));
    }

    @GetMapping("/find/name/{name}")
    public ResponseEntity<Event> getEventByName(@PathVariable("name") String name) {
        User user = authUtil.getUserFromAuthentication();
        return ResponseEntity.of(eventService.findEventByName(name, user));
    }

    @GetMapping("/find/venue/{venueId}")
    public ResponseEntity<List<Event>> getEventsByVenue(@PathVariable("venueId") Integer venueId) {
        User user = authUtil.getUserFromAuthentication();
        return ResponseEntity.ok(eventService.findEventsByVenue(venueId, user));
    }

    @GetMapping("/find/client/{clientId}")
    public ResponseEntity<List<Event>> getEventsByClient(@PathVariable("clientId") Integer clientId) {
        User user = authUtil.getUserFromAuthentication();
        return ResponseEntity.ok(eventService.findEventsByClient(clientId, user));
    }

    @GetMapping("/find/vendor/{vendorId}")
    public ResponseEntity<List<Event>> getEventsByVendor(@PathVariable("vendorId") Integer vendorId) {
        User user = authUtil.getUserFromAuthentication();
        return ResponseEntity.ok(eventService.findEventsByVendor(vendorId, user));
    }

    @PostMapping("/add")
    public ResponseEntity<Event> addEvent(@RequestBody Event event) {
        User user = authUtil.getUserFromAuthentication();
        Event savedEvent = eventService.addEvent(event, user);
        return ResponseEntity.ok(savedEvent);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable("id") Integer id, @RequestBody Event event) {
        User user = authUtil.getUserFromAuthentication();
        try {
            return ResponseEntity.ok(eventService.updateEvent(id, event, user));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable("id") Integer id) {
        User user = authUtil.getUserFromAuthentication();
        boolean deleted = eventService.deleteEvent(id, user);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/rebook/{id}")
    public ResponseEntity<?> rebookEvent(@PathVariable("id") Integer id, @RequestBody Event newEventDetails) {
        User user = authUtil.getUserFromAuthentication();
        try {
            return eventService.rebookEvent(id, newEventDetails, user)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}


