package com.eventvista.event_vista.controller;

import com.eventvista.event_vista.model.Venue;
import com.eventvista.event_vista.model.User;
import com.eventvista.event_vista.data.VenueRepository;
import com.eventvista.event_vista.data.UserRepository;
import com.eventvista.event_vista.security.CustomUserPrincipal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/venues")
@CrossOrigin(origins = "http://localhost:3000")
public class VenueController {

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private UserRepository userRepository;

    private User getUserFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail;

        if (authentication.getPrincipal() instanceof CustomUserPrincipal) {
            CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUser();
        } else if (authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            userEmail = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
            return userRepository.findByEmailAddress(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } else {
            throw new RuntimeException("Unexpected principal type: " + authentication.getPrincipal().getClass());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllVenues() {
        try {
            System.out.println("Getting all venues...");
            User user = getUserFromAuthentication();
            System.out.println("User found: " + user.getEmailAddress());
            List<Venue> venues = venueRepository.findByUser(user);
            System.out.println("Found " + venues.size() + " venues");
            return ResponseEntity.ok(venues);
        } catch (Exception e) {
            System.err.println("Error in getAllVenues: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error fetching venues: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{venueId}")
    public ResponseEntity<?> getVenue(@PathVariable int venueId) {
        try {
            User user = getUserFromAuthentication();

            return venueRepository.findById(venueId)
                    .map(venue -> {
                        if (venue.getUser().getId().equals(user.getId())) {
                            return ResponseEntity.ok(venue);
                        }
                        return ResponseEntity.status(403).body(Map.of("error", "You do not have permission to access this venue"));
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error fetching venue: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping
    public ResponseEntity<?> createVenue(@Valid @RequestBody Venue venue, BindingResult bindingResult) {
        try {
            System.out.println("Creating new venue...");
            System.out.println("Venue data received: " + venue);

            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error -> {
                    System.err.println("Validation error - Field: " + error.getField() + ", Message: " + error.getDefaultMessage());
                    errors.put(error.getField(), error.getDefaultMessage());
                });
                return ResponseEntity.badRequest().body(errors);
            }

            User user = getUserFromAuthentication();
            System.out.println("User found: " + user.getEmailAddress());

            venue.setUser(user);
            Venue savedVenue = venueRepository.save(venue);
            System.out.println("Venue saved successfully with ID: " + savedVenue.getId());
            return ResponseEntity.ok(savedVenue);
        } catch (Exception e) {
            System.err.println("Error in createVenue: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error creating venue: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{venueId}")
    public ResponseEntity<?> updateVenue(@PathVariable int venueId, @Valid @RequestBody Venue updatedVenue, BindingResult bindingResult) {
        try {
            System.out.println("Updating venue with ID: " + venueId);
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error -> {
                    System.err.println("Validation error - Field: " + error.getField() + ", Message: " + error.getDefaultMessage());
                    errors.put(error.getField(), error.getDefaultMessage());
                });
                return ResponseEntity.badRequest().body(errors);
            }

            User user = getUserFromAuthentication();
            System.out.println("User found: " + user.getEmailAddress());

            return venueRepository.findById(venueId)
                    .map(venue -> {
                        if (!venue.getUser().getId().equals(user.getId())) {
                            return ResponseEntity.status(403).body(Map.of("error", "You do not have permission to update this venue"));
                        }
                        venue.setName(updatedVenue.getName());
                        venue.setCapacity(updatedVenue.getCapacity());
                        venue.setEmailAddress(updatedVenue.getEmailAddress());
                        venue.setLocation(updatedVenue.getLocation());
                        venue.setNotes(updatedVenue.getNotes());
                        venue.setPhoneNumber(updatedVenue.getPhoneNumber());
                        Venue saved = venueRepository.save(venue);
                        System.out.println("Venue updated successfully");
                        return ResponseEntity.ok(saved);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            System.err.println("Error in updateVenue: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error updating venue: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{venueId}")
    public ResponseEntity<?> deleteVenue(@PathVariable int venueId) {
        try {
            System.out.println("Deleting venue with ID: " + venueId);
            User user = getUserFromAuthentication();
            System.out.println("User found: " + user.getEmailAddress());

            return venueRepository.findById(venueId)
                    .map(venue -> {
                        if (!venue.getUser().getId().equals(user.getId())) {
                            return ResponseEntity.status(403).body(Map.of("error", "You do not have permission to delete this venue"));
                        }
                        venueRepository.delete(venue);
                        System.out.println("Venue deleted successfully");
                        return ResponseEntity.ok().build();
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            System.err.println("Error in deleteVenue: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error deleting venue: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}

