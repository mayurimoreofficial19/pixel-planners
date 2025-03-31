package com.eventvista.event_vista.data;

import com.eventvista.event_vista.model.PhoneNumber;
import com.eventvista.event_vista.model.Skill;
import com.eventvista.event_vista.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Integer> {
    void deleteVendorById(Integer id);

    Optional<Vendor> findVendorById(Integer id);

    Optional<Vendor> findVendorByName(String name);

    Optional<Vendor> findVendorByLocation(String location);

    Optional<Vendor> findVendorBySkills(Set<Skill> skills);

    Optional<Vendor> findVendorByPhoneNumber(PhoneNumber phoneNumber);

    Optional<Vendor> findVendorByEmailAddress(String emailAddress);
}
