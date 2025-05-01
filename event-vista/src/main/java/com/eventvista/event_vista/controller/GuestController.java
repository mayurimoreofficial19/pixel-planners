package com.eventvista.event_vista.controller;


import com.eventvista.event_vista.model.*;
import com.eventvista.event_vista.service.GuestService;
import com.eventvista.event_vista.service.GuestListService;
import com.eventvista.event_vista.utilities.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/guests")
@CrossOrigin(origins = "http://localhost:3000")
public class GuestController {
    private final GuestService guestService;
    private final GuestListService guestListService;
    private final AuthUtil authUtil;

    public GuestController(GuestService guestService,GuestListService guestListService ,AuthUtil authUtil) {
        this.guestService = guestService;
        this.guestListService = guestListService;
        this.authUtil = authUtil;
    }

    @GetMapping("/all/{guestListId}")
    public ResponseEntity<List<Guest>> getAllGuests(@PathVariable("guestListId") Integer guestListId) {
        User user = authUtil.getUserFromAuthentication();
        Optional<GuestList> guestListOpt = guestListService.findByIdAndUser(guestListId, user);

        return guestListOpt.map(guestList -> {
            List<Guest> guests = guestService.findAllGuestsByGuestList(guestList);
            return ResponseEntity.ok(guests);
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/find/{id}/list/{guestListId}")
    public ResponseEntity<Guest> getGuestById(@PathVariable("id") Integer id, @PathVariable("guestListId") Integer guestListId) {
        User user = authUtil.getUserFromAuthentication();
        return guestListService.findByIdAndUser(guestListId, user)
                .flatMap(guestList -> guestService.findGuestById(id, guestList))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/find/email/{email}/list/{guestListId}")
    public ResponseEntity<Guest> getGuestByEmail(@PathVariable("email") String email, @PathVariable("guestListId") Integer guestListId) {
        User user = authUtil.getUserFromAuthentication();
        return guestListService.findByIdAndUser(guestListId, user)
                .flatMap(guestList -> guestService.findGuestByEmail(email, guestList))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add/{guestListId}")
    public ResponseEntity<?> addGuest(@PathVariable("guestListId") Integer guestListId, @Valid @RequestBody Guest guest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            User user = authUtil.getUserFromAuthentication();
            Optional<GuestList> guestListOpt = guestListService.findByIdAndUser(guestListId, user);

            if (guestListOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Guest newGuest = guestService.addGuest(guest, guestListOpt.get());
            return ResponseEntity.ok(newGuest);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error creating guest: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/update/{id}/list/{guestListId}")
    public ResponseEntity<?> updateGuest(@PathVariable("id") Integer id,
                                         @PathVariable("guestListId") Integer guestListId,
                                         @Valid @RequestBody Guest updatedGuest,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            User user = authUtil.getUserFromAuthentication();
            Optional<GuestList> guestListOpt = guestListService.findByIdAndUser(guestListId, user);

            if (guestListOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            return guestService.updateGuest(id, updatedGuest, guestListOpt.get())
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error updating guest: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/delete/{id}/list/{guestListId}")
    public ResponseEntity<?> deleteGuest(@PathVariable("id") Integer id, @PathVariable("guestListId") Integer guestListId) {
        try {
            User user = authUtil.getUserFromAuthentication();
            Optional<GuestList> guestListOpt = guestListService.findByIdAndUser(guestListId, user);

            if (guestListOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            boolean deleted = guestService.deleteGuest(id, guestListOpt.get());
            if (deleted) {
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
