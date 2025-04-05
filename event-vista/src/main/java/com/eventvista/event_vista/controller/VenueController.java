package com.eventvista.event_vista.controller;

import com.eventvista.event_vista.model.*;
import com.eventvista.event_vista.service.VenueService;
import com.eventvista.event_vista.utilities.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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


//----------------------
    //moving to AuthUtil because this will be reused in all the controllers
//    private User getUserFromAuthentication() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication.getPrincipal() instanceof CustomUserPrincipal) {
//            CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
//            return userPrincipal.getUser();
//        } else if (authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
//            String userEmail = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
//            return userService.findByEmailAddress(userEmail)
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//        } else {
//            throw new RuntimeException("Unexpected principal type: " + authentication.getPrincipal().getClass());
//        }
//    }


// --------------Bringing in additional getVenueBy___-----------------------//
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

        //removed code below because ResponseEntity.of automatically returns 200 Ok if the Optional contains a value
    //and returns 404 Not Found if it is empty ----------------
//        return venueService.findVenueById(id, user)
//                .map(venue -> new ResponseEntity<>(venue, HttpStatus.OK))
//                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
}

    @GetMapping("/find/{name}")
    public ResponseEntity<Venue> getVenueByName (@PathVariable("name") String name) {
        User user = authUtil.getUserFromAuthentication();
        return ResponseEntity.of(venueService.findVenueByName(name, user));
    }

    @GetMapping("/find/{location}")
    public ResponseEntity<Venue> getVenueByLocation (@PathVariable("location") String location) {
        User user = authUtil.getUserFromAuthentication();
        return ResponseEntity.of(venueService.findVenueByLocation(location, user));

    }

    @GetMapping("/find/{phoneNumber}")
    public ResponseEntity<Venue> getVenueByPhoneNumber (@PathVariable("phoneNumber") PhoneNumber phoneNumber) {
        User user = authUtil.getUserFromAuthentication();
        return ResponseEntity.of(venueService.findVenueByPhoneNumber(phoneNumber, user));
    }


    @GetMapping("/find/{emailAddress}")
    public ResponseEntity<Venue> getVenueByEmailAddress (@PathVariable("emailAddress") String emailAddress) {
        User user = authUtil.getUserFromAuthentication();
        return ResponseEntity.of(venueService.findVenueByEmailAddress(emailAddress, user));
    }




    //=-----------------------------------

    //Replacing with getVenueById above
//    @GetMapping("/{venueId}")
//    public ResponseEntity<?> getVenue(@PathVariable int venueId) {
//        try {
//            User user = authUtil.getUserFromAuthentication();
//            return venueService.getVenueById(venueId, user)
//                    .map(ResponseEntity::ok)
//                    .orElse(ResponseEntity.status(403).body((Venue) Map.of("error", "You do not have permission to access this venue")));
//        } catch (Exception e) {
//            Map<String, String> response = new HashMap<>();
//            response.put("error", "Error fetching venue: " + e.getMessage());
//            return ResponseEntity.badRequest().body(response);
//        }
//    }

//    @GetMapping
//    public ResponseEntity<?> getAllVenues() {
//        try {
//            User user = authUtil.getUserFromAuthentication();
//            List<Venue> venues = venueService.getAllVenues(user);
//            return ResponseEntity.ok(venues);
//        } catch (Exception e) {
//            Map<String, String> response = new HashMap<>();
//            response.put("error", "Error fetching venues: " + e.getMessage());
//            return ResponseEntity.badRequest().body(response);
//        }
//    }



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


//    @PostMapping
//    public ResponseEntity<?> createVenue(@Valid @RequestBody Venue venue, BindingResult bindingResult) {
//        try {
//            if (bindingResult.hasErrors()) {
//                Map<String, String> errors = new HashMap<>();
//                bindingResult.getFieldErrors().forEach(error -> {
//                    errors.put(error.getField(), error.getDefaultMessage());
//                });
//                return ResponseEntity.badRequest().body(errors);
//            }
//
//            User user = authUtil.getUserFromAuthentication();
//            Venue savedVenue = venueService.createVenue(venue, user);
//            System.out.println("Venue saved successfully with ID: " + savedVenue.getId());
//            return ResponseEntity.ok(savedVenue);
//        } catch (Exception e) {
//            Map<String, String> response = new HashMap<>();
//            response.put("error", "Error creating venue: " + e.getMessage());
//            return ResponseEntity.badRequest().body(response);
//        }
//    }




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
                    .orElse(ResponseEntity.notFound().build()); // If venue wasn't found
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error updating venue: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }





    //-------------------------
//    @PutMapping("/{venueId}")
//    public ResponseEntity<?> updateVenue(@PathVariable int venueId, @Valid @RequestBody Venue updatedVenue, BindingResult bindingResult) {
//        try {
//            if (bindingResult.hasErrors()) {
//                Map<String, String> errors = new HashMap<>();
//                bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
//                return ResponseEntity.badRequest().body(errors);
//            }
//
//            User user = authUtil.getUserFromAuthentication();
//            return venueService.updateVenue(venueId, updatedVenue, user)
//                    .map(ResponseEntity::ok)
//                    .orElse(ResponseEntity.status(403).body((Venue) Map.of("error", "You do not have permission to update this venue")));
//        } catch (Exception e) {
//            Map<String, String> response = new HashMap<>();
//            response.put("error", "Error updating venue: " + e.getMessage());
//            return ResponseEntity.badRequest().body(response);
//        }
//    }



@DeleteMapping("/delete/{id}")
public ResponseEntity<?> deleteVenue(@PathVariable("id") Integer id) {
    try {
        User user = authUtil.getUserFromAuthentication(); // Get the authenticated user
        boolean isDeleted = venueService.deleteVenue(id, user); // Call the service method

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




//
//    @DeleteMapping("/{venueId}")
//    public ResponseEntity<?> deleteVenue(@PathVariable int venueId) {
//        try {
//            User user = authUtil.getUserFromAuthentication(); // Get the authenticated user
//            boolean isDeleted = venueService.deleteVenue(venueId, user); // Call the service method
//
//            if (isDeleted) {
//                return ResponseEntity.ok().build();
//            } else {
//                return ResponseEntity.status(403).body(Map.of("error", "You do not have permission to delete this venue"));
//            }
//        } catch (Exception e) {
//            Map<String, String> response = new HashMap<>();
//            response.put("error", "Error deleting venue: " + e.getMessage());
//            return ResponseEntity.badRequest().body(response);
//        }
//    }
}

