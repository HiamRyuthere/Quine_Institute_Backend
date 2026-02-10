package com.quine.backend.controller;

import com.quine.backend.model.Student;
import com.quine.backend.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/students")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // 1. Add Student (ADMIN only)
    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        Student savedStudent = studentService.saveStudent(student);
        return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
    }

    // 2. Get All Students (ADMIN + FACULTY)
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    // 3. Get Student by Roll No (Profile Logic with Security Check)
    @GetMapping("/{studentId}")
    public ResponseEntity<?> getStudentByRollNo(@PathVariable String studentId, Authentication authentication) {

        // Login user ka detail nikala
        String loggedInUsername = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        // SECURITY CHECK: Agar role STUDENT hai aur wo dusri ID access kar raha hai
        if (role.equals("ROLE_STUDENT")) {
            if (!loggedInUsername.equals(studentId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Access Denied: Aap sirf apni profile dekh sakte hain.");
            }
        }

        // Data Fetching Logic
        Optional<Student> student = studentService.getStudentByRollNo(studentId);

        if (student.isPresent()) {
            return ResponseEntity.ok(student.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Student nahi mila Roll No: " + studentId);
        }
    }
}