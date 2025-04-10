package com.eventvista.event_vista.data;

import com.eventvista.event_vista.model.Skill;
import com.eventvista.event_vista.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer> {

    List<Skill> findAllByUser(User user);

    Optional<Skill> findByIdAndUser(Integer id, User user);

    Optional<Skill> findByNameAndUser(String name, User user);

    Set<Skill> findAllByIdInAndUser(Set<Integer> ids, User user);
}
