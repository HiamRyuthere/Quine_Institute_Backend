package com.quine.backend.controller;

import com.quine.backend.model.ExamResult;
import com.quine.backend.service.ExamResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/results")
@CrossOrigin(origins = "*")
public class ExamResultController {

    @Autowired
    private ExamResultService examResultService;

    //Marks Upload karne ka API (Role ke hisaab se)
    @PostMapping("/add")
    public String addResult(
            @RequestBody ExamResult result,
            @RequestParam String role // API call karte time batana padega kaun hai (ADMIN/FACULTY)
    ) {
        // Validation Logic
        if (role.equalsIgnoreCase("FACULTY")) {
            // Teacher sirf Internal marks de sakta hai
            if (result.getExternalMarks() != null) {
                return "Error: Aap sirf Internal Marks de sakte hain!";
            }
        }
        else if (role.equalsIgnoreCase("ADMIN")) {
            // Admin sab kuch kar sakta hai (No restriction)
        }
        else {
            return "Error: Student result add nahi kar sakta!";
        }

        examResultService.addResult(result);
        return "Result Saved Successfully via " + role;
    }

    @GetMapping("/student/{studentId}")
    public List<ExamResult> getStudentResults(@PathVariable Long studentId) {
        return examResultService.getResultsByStudent(studentId);
    }
}