package com.eventvista.event_vista.data;

import com.eventvista.event_vista.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface UserRepository extends CrudRepository <User, Integer> {
    User findByUsername(String username);
    User findByEmail(String email);
}
