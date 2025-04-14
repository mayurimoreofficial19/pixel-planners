package com.eventvista.event_vista.controller;

import com.eventvista.event_vista.model.*;
import com.eventvista.event_vista.service.VendorService;
import com.eventvista.event_vista.utilities.AuthUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
@CrossOrigin(origins = "http://localhost:3000")
public class VendorController {

    private final VendorService vendorService;
    private final AuthUtil authUtil;

    // Constructor
    public VendorController(VendorService vendorService, AuthUtil authUtil) {
        this.vendorService = vendorService;
        this.authUtil = authUtil;
    }

    // Mapping
    @GetMapping("/all")
    public ResponseEntity<List<Vendor>> getAllVendors () {
        User user = authUtil.getUserFromAuthentication();
        return ResponseEntity.ok(vendorService.findAllVendors(user));
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

    @GetMapping("/find/skill/{skillId}")
    public ResponseEntity<List<Vendor>> getVendorBySkill (@PathVariable("skillId") Integer skillId) {
        User user = authUtil.getUserFromAuthentication();
        return ResponseEntity.ok(vendorService.findVendorBySkill(skillId, user));
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
    public ResponseEntity<Vendor> addVendor (@RequestBody Vendor vendor) {
        User user = authUtil.getUserFromAuthentication();
        Vendor savedVendor = vendorService.addVendor(vendor, user);
        return ResponseEntity.ok(savedVendor);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Vendor> updateVendor(@PathVariable("id") Integer id, @RequestBody Vendor vendor) {
        User user = authUtil.getUserFromAuthentication();
        try {
            return ResponseEntity.ok(vendorService.updateVendor(id, vendor, user));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVendor (@PathVariable("id") Integer id) {
        User user = authUtil.getUserFromAuthentication();
        boolean deleted = vendorService.deleteVendor(id, user);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
