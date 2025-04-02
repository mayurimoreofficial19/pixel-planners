package com.eventvista.event_vista.service;

import com.eventvista.event_vista.model.User;
import java.util.Optional;

public interface UserService {
    Optional<User> findByEmailAddress(String emailAddress);
    User save(User user);
    boolean existsByEmailAddress(String emailAddress);
}
