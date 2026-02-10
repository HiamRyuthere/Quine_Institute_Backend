package com.quine.backend.controller;

import com.quine.backend.model.Semester;
import com.quine.backend.service.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/semesters")
@CrossOrigin(origins = "*")
public class SemesterController {

    @Autowired
    private SemesterService semesterService;

    // 1. Naya Semester add karna (Admin Only)
    @PostMapping
    public ResponseEntity<Semester> addSemester(@RequestBody Semester semester) {
        return ResponseEntity.ok(semesterService.createSemester(semester));
    }

    // 2. Saare Semesters dekhna
    @GetMapping
    public ResponseEntity<List<Semester>> getAll() {
        return ResponseEntity.ok(semesterService.getAllSemesters());
    }

    // 3. Kisi specific ID ka semester check karna
    @GetMapping("/{id}")
    public ResponseEntity<Semester> getById(@PathVariable Long id) {
        // Simple logic for fetching by ID
        return ResponseEntity.ok(semesterService.getAllSemesters().stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Semester nahi mila!")));
    }
}