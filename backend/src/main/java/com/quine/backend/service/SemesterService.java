package com.quine.backend.service;

import com.quine.backend.model.Semester;
import com.quine.backend.model.Program;
import com.quine.backend.repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SemesterService {

    @Autowired
    private SemesterRepository semesterRepository;

    // Admin manually semester banayega (e.g., BCA Sem 1)
    public Semester createSemester(Semester semester) {
        return semesterRepository.save(semester);
    }

    // Program aur Sem number se search karne wala logic
    public Semester findSemester(Program program, int semNumber) {
        return semesterRepository.findByProgramAndSemNumber(program, semNumber)
                .orElseThrow(() -> new RuntimeException("Bhai, Admin ne " + program.getName() +
                        " ka Semester " + semNumber + " abhi tak create nahi kiya hai!"));
    }

    public List<Semester> getAllSemesters() {
        return semesterRepository.findAll();
    }

    public List<Semester> getSemestersByProgram(Program program) {
        return semesterRepository.findAllByProgram(program);
    }
}