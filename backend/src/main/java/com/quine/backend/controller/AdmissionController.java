package com.quine.backend.controller;

import com.quine.backend.model.Application;
import com.quine.backend.model.Student;
import com.quine.backend.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/applications")
@CrossOrigin(origins = "*")
public class AdmissionController {

    @Autowired
    private ApplicationService applicationService;

    // 1. Apply Now (Public endpoint - No authentication needed for registration)
    @PostMapping("/apply")
    public ResponseEntity<Application> applyNow(@RequestBody Application application) {
        Application savedApp = applicationService.applyNow(application);
        return new ResponseEntity<>(savedApp, HttpStatus.CREATED);
    }

    // 2. Get All Pending Applications (ADMIN only - SecurityConfig me set kar)
    @GetMapping("/pending")
    public ResponseEntity<List<Application>> getPendingApplications() {
        List<Application> pendingApps = applicationService.getPendingApplications();
        return new ResponseEntity<>(pendingApps, HttpStatus.OK);
    }

    // 3. Approve Application (ADMIN only - SecurityConfig me set kar)
    @PutMapping("/approve/{applicationId}")
    public ResponseEntity<?> approveApplication(@PathVariable Long applicationId) {
        try {
            Student newStudent = applicationService.approveApplication(applicationId);
            return ResponseEntity.ok(newStudent);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}