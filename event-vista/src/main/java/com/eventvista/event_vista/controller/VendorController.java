package com.eventvista.event_vista.controller;

import com.eventvista.event_vista.model.PhoneNumber;
import com.eventvista.event_vista.model.Skill;
import com.eventvista.event_vista.model.Vendor;
import com.eventvista.event_vista.model.VendorUpdateRequest;
import com.eventvista.event_vista.service.VendorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/vendor")
public class VendorController {

    private final VendorService vendorService;

    // Constructor
    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    // Mapping
    @GetMapping("/all")
    public ResponseEntity<List<Vendor>> getAllVendors () {
        List<Vendor> vendors = vendorService.findAllVendors();
        return new ResponseEntity<>(vendors, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Vendor> getVendorById (@PathVariable("id") Integer id) {
        Vendor vendor = vendorService.findVendorById(id);
        return new ResponseEntity<>(vendor, HttpStatus.OK);
    }

    @GetMapping("/find/{name}")
    public ResponseEntity<Vendor> getVendorByName (@PathVariable("name") String name) {
        Vendor vendor = vendorService.findVendorByName(name);
        return new ResponseEntity<>(vendor, HttpStatus.OK);
    }

    @GetMapping("/find/{location}")
    public ResponseEntity<Vendor> getVendorByLocation (@PathVariable("location") String location) {
        Vendor vendor = vendorService.findVendorByLocation(location);
        return new ResponseEntity<>(vendor, HttpStatus.OK);
    }

    @GetMapping("/find/{skills}")
    public ResponseEntity<Vendor> getVendorBySkill (@PathVariable("skills") Set<Skill> skills) {
        Vendor vendor = vendorService.findVendorBySkills(skills);
        return new ResponseEntity<>(vendor, HttpStatus.OK);
    }

    @GetMapping("/find/{phoneNumber}")
    public ResponseEntity<Vendor> getVendorByPhoneNumber (@PathVariable("phoneNumber") PhoneNumber phoneNumber) {
        Vendor vendor = vendorService.findVendorByPhoneNumber(phoneNumber);
        return new ResponseEntity<>(vendor, HttpStatus.OK);
    }

    @GetMapping("/find/{emailAddress}")
    public ResponseEntity<Vendor> getVendorByEmailAddress (@PathVariable("emailAddress") String emailAddress) {
        Vendor vendor = vendorService.findVendorByEmailAddress(emailAddress);
        return new ResponseEntity<>(vendor, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Vendor> addVendor (@RequestBody Vendor vendor) {
        Vendor newVendor = vendorService.addVendor(vendor);
        return new ResponseEntity<>(newVendor, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Optional<Vendor>> updateVendor(@RequestBody VendorUpdateRequest request) {
        Optional<Vendor> updatedVendor = vendorService.updateVendor(
                request.getId(),
                request.getName(),
                request.getLocation(),
                request.getSkills(),
                request.getPhoneNumber(),
                request.getEmailAddress()
        );
        return new ResponseEntity<>(updatedVendor, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVendor (@PathVariable("id") Integer id) {
       vendorService.deleteVendor(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
