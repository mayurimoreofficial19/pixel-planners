package com.eventvista.event_vista.controller;

import com.eventvista.event_vista.model.Vendor;
import com.eventvista.event_vista.service.VendorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendor")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

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
    public ResponseEntity<Vendor> updateVendor (@RequestBody Vendor vendor) {
        Vendor updateVendor = vendorService.addVendor(vendor);
        return new ResponseEntity<>(updateVendor, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVendor (@PathVariable("id") Integer id) {
       vendorService.deleteVendor(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
