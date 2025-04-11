package com.eventvista.event_vista.service;

import com.eventvista.event_vista.data.SkillRepository;

import com.eventvista.event_vista.model.Skill;
import com.eventvista.event_vista.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    // Constructor
    @Autowired
    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    // Query methods
    public Skill addSkill(Skill skill, User user) {
        // Set user
        skill.setUser(user);
        return skillRepository.save(skill);
    }

    public Optional<Skill> findSkillById(Integer id, User user) {
        return skillRepository.findByIdAndUser(id, user)
                .filter(skill -> skill.getUser().getId().equals(user.getId()));
    }

    public Optional<Skill> findSkillByName(String name, User user) {
        return skillRepository.findByNameAndUser(name, user)
                .filter(skill -> skill.getUser().getId().equals(user.getId()));
    }

    public List<Skill> findAllSkills(User user) {
        return skillRepository.findAllByUser(user);
    }

    public Skill updateSkill(Integer id, Skill updatedSkill, User user) {
        return skillRepository.findByIdAndUser(id, user)
                .map(existingSkill -> {
                    // Update name
                    existingSkill.setName(updatedSkill.getName());
                    return skillRepository.save(existingSkill);
                })
                .orElseThrow(() -> new RuntimeException("Skill not found"));



    }

    public boolean deleteSkill(Integer id, User user) {
        Optional<Skill> skill = skillRepository.findByIdAndUser(id, user);
        if (skill.isPresent()) {
            skillRepository.delete(skill.get());
            return true;
        }
        return false;
    }
}
