package com.eventvista.event_vista.controller;

import com.eventvista.event_vista.model.*;
import com.eventvista.event_vista.service.VenueService;
import com.eventvista.event_vista.utilities.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/venues")
@CrossOrigin(origins = "http://localhost:3000")
public class VenueController {


    private final VenueService venueService;
    private final AuthUtil authUtil;

    public VenueController(VenueService venueService, AuthUtil authUtil) {
        this.venueService = venueService;
        this.authUtil = authUtil;
    }


    @GetMapping("/all")
    public ResponseEntity<List<Venue>> getAllVenues () {
        User user = authUtil.getUserFromAuthentication();
        List<Venue> venues = venueService.findAllVenues(user);
        return ResponseEntity.ok(venues);
    }


@GetMapping("/find/{id}")
public ResponseEntity<Venue> getVenueById (@PathVariable("id") Integer id) {
        User user = authUtil.getUserFromAuthentication();
        return ResponseEntity.of(venueService.findVenueById(id, user));


}

    @GetMapping("/find/name/{name}")
    public ResponseEntity<Venue> getVenueByName (@PathVariable("name") String name) {
        User user = authUtil.getUserFromAuthentication();
        return ResponseEntity.of(venueService.findVenueByName(name, user));
    }

    @GetMapping("/find/location/{location}")
    public ResponseEntity<Venue> getVenueByLocation (@PathVariable("location") String location) {
        User user = authUtil.getUserFromAuthentication();
        return ResponseEntity.of(venueService.findVenueByLocation(location, user));

    }

    @GetMapping("/find/phone/{phoneNumber}")
    public ResponseEntity<Venue> getVenueByPhoneNumber (@PathVariable("phoneNumber") PhoneNumber phoneNumber) {
        User user = authUtil.getUserFromAuthentication();
        return ResponseEntity.of(venueService.findVenueByPhoneNumber(phoneNumber, user));
    }


    @GetMapping("/find/email/{emailAddress}")
    public ResponseEntity<Venue> getVenueByEmailAddress (@PathVariable("emailAddress") String emailAddress) {
        User user = authUtil.getUserFromAuthentication();
        return ResponseEntity.of(venueService.findVenueByEmailAddress(emailAddress, user));
    }




    @PostMapping("/add")
    public ResponseEntity<?> addVenue (@Valid @RequestBody Venue venue, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error -> {
                    errors.put(error.getField(), error.getDefaultMessage());
                });
                return ResponseEntity.badRequest().body(errors);
            }

            User user = authUtil.getUserFromAuthentication();
            Venue newVenue = venueService.addVenue(venue, user);
            return ResponseEntity.ok(newVenue);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error creating venue: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }




    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateVenue(@PathVariable("id") Integer id, @Valid @RequestBody Venue updatedVenue, BindingResult bindingResult) {

            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
                return ResponseEntity.badRequest().body(errors);
            }
        try {
            User user = authUtil.getUserFromAuthentication();
            return venueService.updateVenue(id, updatedVenue, user)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error updating venue: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }



@DeleteMapping("/delete/{id}")
public ResponseEntity<?> deleteVenue(@PathVariable("id") Integer id) {
    try {
        User user = authUtil.getUserFromAuthentication();
        boolean isDeleted = venueService.deleteVenue(id, user);

        if (isDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    } catch (Exception e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Error deleting venue: " + e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}
}

