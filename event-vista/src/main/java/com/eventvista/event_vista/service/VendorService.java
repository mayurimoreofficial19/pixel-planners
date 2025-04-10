package com.eventvista.event_vista.service;


import com.eventvista.event_vista.data.VendorRepository;
import com.eventvista.event_vista.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VendorService {

    private final VendorRepository vendorRepository;
    private final SkillService skillService;

    // Constructor
    @Autowired
    public VendorService(VendorRepository vendorRepository, SkillService skillService) {
        this.vendorRepository = vendorRepository;
        this.skillService = skillService;
    }


    // Query methods
    public Vendor addVendor(Vendor vendor, User user) {
        // Set the user
        vendor.setUser(user);

        // Handle Skill relationship
        if (vendor.getSkill() != null && vendor.getSkill().getId() != null) {
            skillService.findSkillById(vendor.getSkill().getId(), user)
                    .ifPresent(vendor::setSkill);
        } else {
            vendor.setSkill(null);
        }

        return vendorRepository.save(vendor);
    }

    public Optional<Vendor> findVendorById(Integer id, User user) {
        return vendorRepository.findByIdAndUser(id, user);
    }

    public Optional<Vendor> findVendorByName(String name, User user) {
        return vendorRepository.findByNameAndUser(name, user);
    }

    public Optional<Vendor> findVendorByLocation(String location, User user) {
        return vendorRepository.findByLocationAndUser(location, user);
    }

    public List<Vendor> findVendorBySkill(Integer skillId, User user) {
        return vendorRepository.findBySkillIdAndUser(skillId, user);
    }

    public Optional<Vendor> findVendorByPhoneNumber(PhoneNumber phoneNumber, User user) {
        return vendorRepository.findByPhoneNumberAndUser(phoneNumber, user);
    }

    public Optional<Vendor> findVendorByEmailAddress(String emailAddress, User user) {
        return vendorRepository.findByEmailAddressAndUser(emailAddress, user);
    }

    public List<Vendor> findAllVendors(User user) {
        return vendorRepository.findAllByUser(user);
    }

    public Vendor updateVendor(Integer id, Vendor updatedVendor, User user) {
        return vendorRepository.findById(id)
                .map(existingVendor -> {
                    // Update basic fields
                    existingVendor.setName(updatedVendor.getName());
                    existingVendor.setLocation(updatedVendor.getLocation());
                    existingVendor.setPhoneNumber(updatedVendor.getPhoneNumber());
                    existingVendor.setEmailAddress(updatedVendor.getEmailAddress());

                    // Handle Skill relationship
                    if (updatedVendor.getSkill() != null && updatedVendor.getSkill().getId() != null) {
                        skillService.findSkillById(updatedVendor.getSkill().getId(), user)
                                .isPresent();
                    }
                    return vendorRepository.save(existingVendor);
                })
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
    }

    public boolean deleteVendor(Integer id, User user) {
        Optional<Vendor> vendor = vendorRepository.findByIdAndUser(id, user);
        if (vendor.isPresent()) {
            vendorRepository.delete(vendor.get());
            return true;
        }
        return false;
    }


}
