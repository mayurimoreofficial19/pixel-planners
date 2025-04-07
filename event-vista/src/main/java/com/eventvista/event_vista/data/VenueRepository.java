package com.eventvista.event_vista.data;

import com.eventvista.event_vista.model.PhoneNumber;
import com.eventvista.event_vista.model.Venue;
import com.eventvista.event_vista.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Integer> {


    // searching by user everytime so each user can only find their own venues

    List<Venue> findAllByUser(User user);

    Optional<Venue> findByIdAndUser(Integer id, User user);

    Optional<Venue> findByNameAndUser(String name, User user);

    Optional<Venue> findByLocationAndUser(String location, User user);

    Optional<Venue> findByEmailAddressAndUser(String emailAddress, User user);

    Optional<Venue> findByPhoneNumberAndUser(PhoneNumber phoneNumber, User user);


}