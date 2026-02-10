package com.quine.backend.controller;

import com.quine.backend.model.Subject;
import com.quine.backend.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/subjects")
@CrossOrigin(origins = "*")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @PostMapping
    public ResponseEntity<Subject> create(@RequestBody Subject subject) {
        return ResponseEntity.ok(subjectService.addSubject(subject));
    }

    @GetMapping
    public ResponseEntity<List<Subject>> getAll() {
        return ResponseEntity.ok(subjectService.getAllSubjects());
    }

    @GetMapping("/program/{programId}")
    public ResponseEntity<List<Subject>> getByProgram(@PathVariable Long programId) {
        return ResponseEntity.ok(subjectService.getSubjectsByProgram(programId));
    }
}