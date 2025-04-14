package com.eventvista.event_vista.service;


import com.eventvista.event_vista.data.SkillRepository;
import com.eventvista.event_vista.data.VendorRepository;
import com.eventvista.event_vista.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VendorService {

    private final VendorRepository vendorRepository;
    private final SkillRepository skillRepository;

    // Constructor
    @Autowired
    public VendorService(VendorRepository vendorRepository, SkillRepository skillRepository) {
        this.vendorRepository = vendorRepository;
        this.skillRepository= skillRepository;
    }


    // Query methods
    public Vendor addVendor(Vendor vendor, User user) {
        // Set the user
        vendor.setUser(user);

        // Handle Skill relationship
        List<Skill> incomingSkills = vendor.getSkills();
        if (incomingSkills != null && !incomingSkills.isEmpty()) {
            List<Skill> validSkills = incomingSkills.stream()
                    .map(Skill::getId)
                    .filter(Objects::nonNull)
                    .map(id -> skillRepository.findByIdAndUser(id, user))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

            vendor.setSkills(validSkills);
        } else {
            vendor.setSkills(null);
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
        return vendorRepository.findBySkillsIdAndUser(skillId, user);
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
        return vendorRepository.findByIdAndUser(id, user)
                .map(existingVendor -> {
                    // Update basic fields
                    existingVendor.setName(updatedVendor.getName());
                    existingVendor.setLocation(updatedVendor.getLocation());
                    existingVendor.setPhoneNumber(updatedVendor.getPhoneNumber());
                    existingVendor.setEmailAddress(updatedVendor.getEmailAddress());
                    existingVendor.setNotes(updatedVendor.getNotes());

                    // Handle Skill relationship
                    List<Skill> incomingSkills = updatedVendor.getSkills();
                    if (incomingSkills != null && !incomingSkills.isEmpty()) {
                        List<Skill> validSkills = incomingSkills.stream()
                                .map(Skill::getId)
                                .filter(Objects::nonNull)
                                .map(skillId -> skillRepository.findByIdAndUser(skillId, user))
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .collect(Collectors.toList());

                        existingVendor.setSkills(validSkills);
                    } else {
                        existingVendor.setSkills(null);
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
