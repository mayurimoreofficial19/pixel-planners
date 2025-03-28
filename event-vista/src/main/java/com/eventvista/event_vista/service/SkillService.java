package com.eventvista.event_vista.service;

import com.eventvista.event_vista.data.SkillRepository;
import com.eventvista.event_vista.exception.SkillNotFoundException;
import com.eventvista.event_vista.model.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    // Constructor
    @Autowired
    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    // Query methods
    public Skill addSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    public Skill findSkillById(Integer id) {
        return skillRepository.findSkillById(id).orElseThrow(() -> new SkillNotFoundException("Skill by id " + id + " was not found."));
    }

    public Skill findSkillByName(String name) {
        return skillRepository.findSkillByName(name).orElseThrow(() -> new SkillNotFoundException("Skill by name " + name + " was not found."));
    }

    public List<Skill> findAllSkills() {
        return skillRepository.findAll();
    }

    public Optional<Skill> updateSkill(Integer id, String name) {

        // Logic to find the skill and update
        Skill skill = findSkillById(id); // Retrieve the skill by ID
        if (name != null) skill.setName(name);
        // Save the updated skill
        return Optional.of(skillRepository.save(skill));
    }
    public void deleteSkill(Integer id) {
        skillRepository.deleteSkillById(id);
    }


}
