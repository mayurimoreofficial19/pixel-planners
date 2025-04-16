package com.eventvista.event_vista.data;

import com.eventvista.event_vista.model.PhoneNumber;
import com.eventvista.event_vista.model.User;
import com.eventvista.event_vista.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Integer> {

    List<Vendor> findAllByUser(User user);

    Optional<Vendor> findByIdAndUser(Integer id, User user);

    Optional<Vendor> findByNameAndUser(String name, User user);

    Optional<Vendor> findByLocationAndUser(String location, User user);

    List<Vendor> findBySkillsIdAndUser(Integer skillId, User user);

    Optional<Vendor> findByPhoneNumberAndUser(PhoneNumber phoneNumber, User user);

    Optional<Vendor> findByEmailAddressAndUser(String emailAddress, User user);
}
