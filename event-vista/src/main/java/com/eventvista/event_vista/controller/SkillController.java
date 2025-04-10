package com.eventvista.event_vista.controller;

import com.eventvista.event_vista.model.Skill;
import com.eventvista.event_vista.model.User;
import com.eventvista.event_vista.service.SkillService;
import com.eventvista.event_vista.utilities.AuthUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@CrossOrigin(origins = "http://localhost:3000")
public class SkillController {

    private final SkillService skillService;
    private final AuthUtil authUtil;


    // Constructor
    public SkillController(SkillService skillService, AuthUtil authUtil) {
        this.skillService = skillService;
        this.authUtil = authUtil;
    }

    // Mapping
    @GetMapping("/all")
    public ResponseEntity<List<Skill>> getAllSkills () {
        User user = authUtil.getUserFromAuthentication();
        return ResponseEntity.ok(skillService.findAllSkills(user));
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Skill> getSkillById (@PathVariable("id") Integer id) {
        User user = authUtil.getUserFromAuthentication();
        return ResponseEntity.of(skillService.findSkillById(id, user));
    }

    @GetMapping("/find/name/{name}")
    public ResponseEntity<Skill> getSkillByName (@PathVariable("name") String name) {
        User user = authUtil.getUserFromAuthentication();
        return ResponseEntity.of(skillService.findSkillByName(name, user));
    }

    @PostMapping("/add")
    public ResponseEntity<Skill> addSkill (@RequestBody Skill skill) {
        User user = authUtil.getUserFromAuthentication();
        Skill savedSkill = skillService.addSkill(skill, user);
        return ResponseEntity.ok(savedSkill);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Skill> updateSkill(@PathVariable("id") Integer id, @RequestBody Skill skill) {
        User user = authUtil.getUserFromAuthentication();
        try {
            return ResponseEntity.ok(skillService.updateSkill(id, skill, user));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSkill (@PathVariable("id") Integer id) {
        User user = authUtil.getUserFromAuthentication();
        boolean deleted = skillService.deleteSkill(id, user);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
