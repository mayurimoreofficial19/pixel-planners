package com.eventvista.event_vista.data;

import com.eventvista.event_vista.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer> {
    void deleteSkillById(Integer id);

    Optional<Skill> findSkillById(Integer id);

    Optional<Skill> findSkillByName(String name);
}
