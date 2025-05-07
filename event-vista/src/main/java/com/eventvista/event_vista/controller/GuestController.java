package com.eventvista.event_vista.controller;

import com.eventvista.event_vista.model.Client;
import com.eventvista.event_vista.model.Guest;
import com.eventvista.event_vista.model.PhoneNumber;
import com.eventvista.event_vista.model.User;
import com.eventvista.event_vista.service.ClientService;
import com.eventvista.event_vista.service.GuestService;
import com.eventvista.event_vista.utilities.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/guests")
@CrossOrigin(origins = "http://localhost:3000")
public class GuestController {
    private final GuestService guestService;
    private final AuthUtil authUtil ;

    public GuestController(GuestService guestService, AuthUtil authUtil) {
        this.guestService = guestService;
        this.authUtil = authUtil;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Guest>> getAllGuests () {
        User user = authUtil.getUserFromAuthentication();
        List<Guest> guests = guestService.findAllGuests(user);
        return ResponseEntity.ok(guests);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Guest> getGuestById (@PathVariable("id") Integer id) {
        User user = authUtil.getUserFromAuthentication();
        return ResponseEntity.of(guestService.findGuestById(id, user));
    }

    @GetMapping("/find/name/{name}")
    public ResponseEntity<Guest> getGuestByName (@PathVariable("name") String name) {
        User user = authUtil.getUserFromAuthentication();
        return ResponseEntity.of(guestService.findGuestByName(name, user));
    }

    @GetMapping("/find/email/{emailAddress}")
    public ResponseEntity<Guest> getGuestByEmailAddress (@PathVariable("emailAddress") String emailAddress) {
        User user = authUtil.getUserFromAuthentication();
        return ResponseEntity.of(guestService.findGuestByEmailAddress(emailAddress, user));
    }

    @GetMapping("/find/status/{status}")
    public ResponseEntity<?> getGuestsByRsvpStatus(@PathVariable("status") String status) {
        try {
            User user = authUtil.getUserFromAuthentication();
            List<Guest> guests = guestService.findGuestsByRsvpStatus(status.toUpperCase(), user);
            return ResponseEntity.ok(guests);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid RSVP status: " + status);
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to retrieve guests: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addGuest (@Valid @RequestBody Guest guest, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error -> {
                    errors.put(error.getField(), error.getDefaultMessage());
                });
                return ResponseEntity.badRequest().body(errors);
            }

            User user = authUtil.getUserFromAuthentication();
            Guest newGuest = guestService.addGuest(guest, user);
            return ResponseEntity.ok(newGuest);
        } catch (DataIntegrityViolationException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Duplicate name or email address");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error creating guest: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateGuest(@PathVariable("id") Integer id, @Valid @RequestBody Guest updatedGuest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            User user = authUtil.getUserFromAuthentication();
            return guestService.updateGuest(id, updatedGuest, user)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error updating guest: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteGuest(@PathVariable("id") Integer id) {
        try {
            User user = authUtil.getUserFromAuthentication();
            boolean isDeleted = guestService.deleteGuest(id, user);

            if (isDeleted) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error deleting guest: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
