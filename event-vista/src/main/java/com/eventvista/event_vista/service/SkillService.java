package com.eventvista.event_vista.service;

import com.eventvista.event_vista.data.SkillRepository;
import com.eventvista.event_vista.exception.SkillNotFoundException;
import com.eventvista.event_vista.model.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    // Constructor
    @Autowired
    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    // Query methods
    public Skill findSkillById(Integer id) {
        return skillRepository.findSkillById(id).orElseThrow(() -> new SkillNotFoundException("Skill by id " + id + " was not found."));
    }

    public List<Skill> findAllSkills() {
        return skillRepository.findAll();
    }

    public Skill updateSkill(Skill skill) {
        return skillRepository.save(skill);
    }


    public void deleteSkill(Integer id) {
        skillRepository.deleteSkillById(id);
    }


}
