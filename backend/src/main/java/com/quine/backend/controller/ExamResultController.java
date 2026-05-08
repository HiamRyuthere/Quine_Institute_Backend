package com.quine.backend.controller;

import com.quine.backend.model.ExamResult;
import com.quine.backend.model.Semester;
import com.quine.backend.model.Student;
import com.quine.backend.model.Subject;
import com.quine.backend.repository.ExamResultRepository;
import com.quine.backend.repository.SemesterRepository;
import com.quine.backend.repository.StudentRepository;
import com.quine.backend.repository.SubjectRepository;
import com.quine.backend.service.ExamResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/results")
@CrossOrigin(origins = "*")
public class ExamResultController {

    @Autowired private ExamResultService    examResultService;
    @Autowired private ExamResultRepository examResultRepository;
    @Autowired private StudentRepository    studentRepository;
    @Autowired private SemesterRepository   semesterRepository;
    @Autowired private SubjectRepository    subjectRepository;

    // ─────────────────────────────────────────────────────────────────────
    // POST /api/v1/results/add?role=ADMIN|FACULTY|EXAMINER
    // ─────────────────────────────────────────────────────────────────────
    @PostMapping("/add")
    public ResponseEntity<String> addResult(
            @RequestBody ExamResult result,
            @RequestParam String role
    ) {
        switch (role.toUpperCase()) {

            case "FACULTY":
                // Faculty sirf internal marks de sakta hai
                if (result.getExternalMarks() != null && result.getExternalMarks() > 0)
                    return ResponseEntity.badRequest()
                            .body("Error: Faculty sirf Internal Marks de sakta hai!");
                break;

            case "EXAMINER":
                // Examiner sirf external marks de sakta hai
                if (result.getInternalMarks() != null && result.getInternalMarks() > 0)
                    return ResponseEntity.badRequest()
                            .body("Error: Examiner sirf External Marks de sakta hai!");
                break;

            case "ADMIN":
                // Admin — no restriction
                break;

            default:
                return ResponseEntity.badRequest()
                        .body("Error: Invalid role. Use ADMIN, FACULTY, or EXAMINER.");
        }

        // Result save karo — service mein pass/fail auto-calculate hota hai (total < 33 = FAIL)
        ExamResult saved = examResultService.addResult(result);

        // Auto-promotion check
        String promotionMsg = checkAndPromote(saved);

        return ResponseEntity.ok("Result saved via " + role + promotionMsg);
    }

    // ─────────────────────────────────────────────────────────────────────
    // GET /api/v1/results/student/{studentId}
    // studentId = student ka DB id (Long), roll number nahi
    // ─────────────────────────────────────────────────────────────────────
    @GetMapping("/student/{studentId}")
    public List<ExamResult> getStudentResults(@PathVariable Long studentId) {
        return examResultService.getResultsByStudent(studentId);
    }

    // ─────────────────────────────────────────────────────────────────────
    // AUTO-PROMOTION LOGIC
    //
    // ExamResult mein semester field nahi hai —
    // semester nikalta hai: result -> subject -> semester (ManyToOne chain)
    //
    // Rule: Current semester ke 60%+ subjects PASS → next semester promote
    // ─────────────────────────────────────────────────────────────────────
    private String checkAndPromote(ExamResult saved) {

        // Student fetch
        if (saved.getStudent() == null) return "";
        Student student = studentRepository.findById(saved.getStudent().getId())
                .orElse(null);
        if (student == null || student.getSemester() == null) return "";

        Semester currentSem = student.getSemester();

        // Current semester ke saare subjects
        List<Subject> semSubjects = subjectRepository.findBySemester(currentSem);
        if (semSubjects.isEmpty()) return "";

        long totalSubjects = semSubjects.size();

        // Student ke saare results
        // findByStudentId → student.id (Long PK) use karta hai
        List<ExamResult> allResults = examResultRepository.findByStudentId(student.getId());

        // Current semester wale results filter karo
        // subject -> semester chain se check karo (ExamResult mein semester nahi hai)
        long passedSubjects = allResults.stream()
                .filter(r ->
                        r.getSubject() != null &&
                                r.getSubject().getSemester() != null &&
                                r.getSubject().getSemester().getId().equals(currentSem.getId()) &&
                                "PASS".equalsIgnoreCase(r.getStatus())
                )
                .count();

        // 60% threshold check
        long required = (long) Math.ceil(totalSubjects * 0.6);
        if (passedSubjects < required) return "";

        // Program complete check
        int nextSemNum = currentSem.getSemNumber() + 1;
        if (nextSemNum > currentSem.getProgram().getTotalSemesters())
            return " | 🎓 Program complete! No more semesters.";

        // Next semester DB mein hai?
        Semester nextSem = semesterRepository
                .findByProgramAndSemNumber(currentSem.getProgram(), nextSemNum)
                .orElse(null);
        if (nextSem == null) return "";

        // Promote!
        student.setSemester(nextSem);
        studentRepository.save(student);

        return String.format(
                " | ✅ AUTO-PROMOTED: %s — Sem %d → Sem %d",
                student.getName(),
                currentSem.getSemNumber(),
                nextSemNum
        );
    }
}
