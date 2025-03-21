package com.eventvista.event_vista.data;

import com.eventvista.event_vista.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    User findByEmailAddress(String emailAddress);
}
