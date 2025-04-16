package com.eventvista.event_vista.service;

import com.eventvista.event_vista.data.SkillRepository;

import com.eventvista.event_vista.data.VendorRepository;
import com.eventvista.event_vista.model.Skill;
import com.eventvista.event_vista.model.User;
import com.eventvista.event_vista.model.Vendor;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class SkillService {

    private final SkillRepository skillRepository;
    private final VendorRepository vendorRepository;
    private final VendorService vendorService;

    // Constructor
    @Autowired
    public SkillService(SkillRepository skillRepository, VendorRepository vendorRepository, VendorService vendorService) {
        this.skillRepository = skillRepository;
        this.vendorRepository = vendorRepository;
        this.vendorService = vendorService;
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

    @Transactional
    public boolean deleteSkill(Integer skillId, User user) {
        Optional<Skill> skill = skillRepository.findByIdAndUser(skillId, user);
        if (skill.isPresent()) {
            skillRepository.delete(skill.get());
            return true;
        }
        return false;
    }
}
