package com.quine.backend.service;

import com.quine.backend.model.Mentor;
import com.quine.backend.model.Semester;
import com.quine.backend.model.SectionEnum;
import com.quine.backend.repository.MentorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MentorService {

    @Autowired
    private MentorRepository mentorRepository;

    // Admin naya mentor link karega (Employee + Sem + Section)
    public Mentor saveMentor(Mentor mentor) {
        return mentorRepository.save(mentor);
    }

    // RYU LOGIC: Semester aur Section se Mentor dhoondho [cite: 2025-12-15]
    public Mentor findMentorBySemAndSection(Semester semester, SectionEnum section) {
        return mentorRepository.findBySemesterAndSection(semester, section)
                .orElseThrow(() -> new RuntimeException("Bhai, is Semester aur Section ke liye koi Mentor assigned nahi hai!"));
    }
}