package com.eventvista.event_vista.data;

import com.eventvista.event_vista.model.GuestList;
import com.eventvista.event_vista.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GuestListRepository extends JpaRepository<GuestList, Integer> {
    List<GuestList> findAllByEvent_User(User user);
}
