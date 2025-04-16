package com.eventvista.event_vista.controller;

import com.eventvista.event_vista.model.*;
import com.eventvista.event_vista.service.SkillService;
import com.eventvista.event_vista.service.VendorService;
import com.eventvista.event_vista.utilities.AuthUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vendors")
@CrossOrigin(origins = "http://localhost:3000")
public class VendorController {

    private final VendorService vendorService;
    private final SkillService skillService;
    private final AuthUtil authUtil;

    // Constructor
    public VendorController(VendorService vendorService, SkillService skillService, AuthUtil authUtil) {
        this.vendorService = vendorService;
        this.skillService = skillService;
        this.authUtil = authUtil;
    }

    // Mapping
    @GetMapping("/all")
    public ResponseEntity<List<Vendor>> getAllVendors () {
        User user = authUtil.getUserFromAuthentication();
        List<Vendor> vendors = vendorService.findAllVendors(user);
        return ResponseEntity.ok(vendors);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Vendor> getVendorById (@PathVariable("id") Integer id) {
        User user = authUtil.getUserFromAuthentication();
        return ResponseEntity.of(vendorService.findVendorById(id, user));
    }

    @GetMapping("/find/name/{name}")
    public ResponseEntity<Vendor> getVendorByName (@PathVariable("name") String name) {
        User user = authUtil.getUserFromAuthentication();
        return ResponseEntity.of(vendorService.findVendorByName(name, user));
    }

    @GetMapping("/find/location/{location}")
    public ResponseEntity<Vendor> getVendorByLocation (@PathVariable("location") String location) {
        User user = authUtil.getUserFromAuthentication();
        return ResponseEntity.of(vendorService.findVendorByLocation(location, user));
    }

    // Search vendors by skill ID
    @GetMapping("/find/skills/id/{skillId}")
    public ResponseEntity<List<Vendor>> getVendorBySkillId(@PathVariable("skillId") Integer skillId) {
        User user = authUtil.getUserFromAuthentication();
        return ResponseEntity.ok(vendorService.findVendorBySkillId(skillId, user));
    }

    // Search vendors by skill name
    @GetMapping("/find/skills/name/{skillName}")
    public ResponseEntity<List<Vendor>> getVendorBySkillName(@PathVariable("skillName") String skillName) {
        User user = authUtil.getUserFromAuthentication();
        return ResponseEntity.ok(vendorService.findVendorBySkillName(skillName, user));
    }

    @GetMapping("/find/phone/{phoneNumber}")
    public ResponseEntity<Vendor> getVendorByPhoneNumber (@PathVariable("phoneNumber") PhoneNumber phoneNumber) {
        User user = authUtil.getUserFromAuthentication();
        return ResponseEntity.of(vendorService.findVendorByPhoneNumber(phoneNumber, user));
    }

    @GetMapping("/find/email/{emailAddress}")
    public ResponseEntity<Vendor> getVendorByEmailAddress (@PathVariable("emailAddress") String emailAddress) {
        User user = authUtil.getUserFromAuthentication();
        return ResponseEntity.of(vendorService.findVendorByEmailAddress(emailAddress, user));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addVendor (@Valid @RequestBody Vendor vendor, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error -> {
                    errors.put(error.getField(), error.getDefaultMessage());
                });
                return ResponseEntity.badRequest().body(errors);
            }

            User user = authUtil.getUserFromAuthentication();
            Vendor newVendor = vendorService.addVendor(vendor, user);
            return ResponseEntity.ok(newVendor);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error creating vendor: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateVendor(@PathVariable("id") Integer id, @Valid @RequestBody Vendor updatedVendor, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            User user = authUtil.getUserFromAuthentication();
            return vendorService.updateVendor(id, updatedVendor, user)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error updating vendor: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVendor(@PathVariable("id") Integer id) {
        try {
            User user = authUtil.getUserFromAuthentication();
            boolean isDeleted = vendorService.deleteVendor(id, user);

            if (isDeleted) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error deleting vendor: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/delete/skills/{skillId}")
    @Transactional
    public ResponseEntity<?> removeSkillFromVendors(@PathVariable Integer skillId) {
        User user = authUtil.getUserFromAuthentication();
        try {
            vendorService.removeSkillFromVendors(skillId, user);
            boolean deleted = skillService.deleteSkill(skillId, user);

            return deleted
                    ? ResponseEntity.ok("Skill removed from vendors and deleted.")
                    : ResponseEntity.notFound().build();

        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
