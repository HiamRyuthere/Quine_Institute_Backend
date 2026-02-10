package com.quine.backend.service;

import com.quine.backend.model.Subject;
import com.quine.backend.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    public Subject addSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    // FIXED: Ab hum nested property call kar rahe hain
    public List<Subject> getSubjectsByProgram(Long programId) {
        return subjectRepository.findBySemester_Program_Id(programId);
    }
}