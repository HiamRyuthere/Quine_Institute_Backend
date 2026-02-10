package com.quine.backend.controller;

import com.quine.backend.model.Mentor;
import com.quine.backend.model.Semester;
import com.quine.backend.model.SectionEnum;
import com.quine.backend.service.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/mentors")
@CrossOrigin(origins = "*")
public class MentorController {

    @Autowired
    private MentorService mentorService;

    // 1. Admin Mentor assign karega
    @PostMapping
    public ResponseEntity<Mentor> addMentor(@RequestBody Mentor mentor) {
        return ResponseEntity.ok(mentorService.saveMentor(mentor));
    }

    // 2. Student apna mentor dhoondhega (Using Query Params) [cite: 2025-06-23]
    @GetMapping("/find")
    public ResponseEntity<Mentor> getMentor(
            @RequestParam Long semesterId,
            @RequestParam SectionEnum section) {

        Semester tempSem = new Semester();
        tempSem.setId(semesterId);

        return ResponseEntity.ok(mentorService.findMentorBySemAndSection(tempSem, section));
    }
}