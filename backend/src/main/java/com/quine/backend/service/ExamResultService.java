package com.quine.backend.service;

import com.quine.backend.model.ExamResult;
import com.quine.backend.model.Student;
import com.quine.backend.model.Subject;
import com.quine.backend.repository.ExamResultRepository;
import com.quine.backend.repository.StudentRepository;
import com.quine.backend.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExamResultService {

    @Autowired private ExamResultRepository examResultRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private SubjectRepository subjectRepository;

    public ExamResult addResult(ExamResult result) {

        // 1. Student Check
        if (result.getStudent() != null && result.getStudent().getId() != null) {
            Student student = studentRepository.findById(result.getStudent().getId())
                    .orElseThrow(() -> new RuntimeException("Bhai, Student nahi mila!"));
            result.setStudent(student);
        }

        // 2. Subject Check
        if (result.getSubject() != null && result.getSubject().getId() != null) {
            Subject subject = subjectRepository.findById(result.getSubject().getId())
                    .orElseThrow(() -> new RuntimeException("Bhai, Subject nahi mila!"));
            result.setSubject(subject);
        }

        // 3. Pass/Fail Logic (Reality Check: 33 is the boundary)
        int internal = (result.getInternalMarks() != null) ? result.getInternalMarks() : 0;
        int external = (result.getExternalMarks() != null) ? result.getExternalMarks() : 0;
        int total = internal + external;

        if (total < 33) {
            result.setStatus("FAIL");
        } else {
            result.setStatus("PASS");
        }

        return examResultRepository.save(result);
    }

    public List<ExamResult> getResultsByStudent(Long studentId) {
        // Ensure your ExamResultRepository has this method
        return examResultRepository.findByStudentId(studentId);
    }
}