package com.eventvista.event_vista.controller;

import com.eventvista.event_vista.model.Skill;
import com.eventvista.event_vista.model.SkillUpdateRequest;
import com.eventvista.event_vista.service.SkillService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/skill")
public class SkillController {

    private final SkillService skillService;


    // Constructor
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    // Mapping
    @GetMapping("/all")
    public ResponseEntity<List<Skill>> getAllSkills () {
        List<Skill> skills = skillService.findAllSkills();
        return new ResponseEntity<>(skills, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Skill> getSkillById (@PathVariable("id") Integer id) {
        Skill skill = skillService.findSkillById(id);
        return new ResponseEntity<>(skill, HttpStatus.OK);
    }

    @GetMapping("/find/{name}")
    public ResponseEntity<Skill> getSkillByName (@PathVariable("name") String name) {
        Skill skill = skillService.findSkillByName(name);
        return new ResponseEntity<>(skill, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Skill> addSkill (@RequestBody Skill skill) {
        Skill newSkill = skillService.addSkill(skill);
        return new ResponseEntity<>(newSkill, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Optional<Skill>> updateSkill(@RequestBody SkillUpdateRequest request) {
        Optional<Skill> updatedSkill = skillService.updateSkill(
                request.getId(),
                request.getName()
        );
        return new ResponseEntity<>(updatedSkill, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSkill (@PathVariable("id") Integer id) {
        skillService.deleteSkill(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
