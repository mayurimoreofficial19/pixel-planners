package com.eventvista.event_vista.service;

import com.eventvista.event_vista.data.VendorRepository;
import com.eventvista.event_vista.exception.VendorNotFoundException;
import com.eventvista.event_vista.model.PhoneNumber;
import com.eventvista.event_vista.model.Skill;
import com.eventvista.event_vista.model.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class VendorService {

    private final VendorRepository vendorRepository;

    // Constructor
    @Autowired
    public VendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }


    // Query methods
    public Vendor addVendor(Vendor vendor) {
        return vendorRepository.save(vendor);
    }

    public Vendor findVendorById(Integer id) {
        return vendorRepository.findVendorById(id).orElseThrow(() -> new VendorNotFoundException("Vendor by id " + id + " was not found."));
    }

    public Vendor findVendorByName(String name) {
        return vendorRepository.findVendorByName(name).orElseThrow(() -> new VendorNotFoundException("Vendor by name " + name + " was not found."));
    }

    public Vendor findVendorByLocation(String location) {
        return vendorRepository.findVendorByLocation(location).orElseThrow(() -> new VendorNotFoundException("Vendor by location " + location + " was not found."));
    }

    public Vendor findVendorBySkills(Set<Skill> skills) {
        return vendorRepository.findVendorBySkills(skills).orElseThrow(() -> new VendorNotFoundException("Vendor by skill " + skills + " was not found."));
    }

    public Vendor findVendorByPhoneNumber(PhoneNumber phoneNumber) {
        return vendorRepository.findVendorByPhoneNumber(phoneNumber).orElseThrow(() -> new VendorNotFoundException("Vendor by phone number " + phoneNumber + " was not found."));
    }

    public Vendor findVendorByEmailAddress(String emailAddress) {
        return vendorRepository.findVendorByEmailAddress(emailAddress).orElseThrow(() -> new VendorNotFoundException("Vendor by email address " + emailAddress + " was not found."));
    }

    public List<Vendor> findAllVendors() {
        return vendorRepository.findAll();
    }

    public Optional<Vendor> updateVendor(Integer id, String name, String location, Set<Skill> skills, PhoneNumber phoneNumber, String emailAddress) {

        // Logic to find the vendor and update only the provided fields
        Vendor vendor = findVendorById(id); // Retrieve the vendor by ID
        if (name != null) vendor.setName(name);
        if (location != null) vendor.setLocation(location);
        if (skills != null) vendor.setSkills(skills);
        if (phoneNumber != null) vendor.setPhoneNumber(phoneNumber);
        if (emailAddress != null) vendor.setEmailAddress(emailAddress);

        // Save the updated vendor
        return Optional.of(vendorRepository.save(vendor));
    }

    public void deleteVendor(Integer id) {
        vendorRepository.deleteVendorById(id);
    }


}
