package com.quine.backend.service;

import com.quine.backend.model.Program;
import com.quine.backend.repository.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProgramService {

    @Autowired
    private ProgramRepository programRepository;

    public Program addProgram(Program program) {
        return programRepository.save(program);
    }

    public List<Program> getAllPrograms() {
        return programRepository.findAll();
    }
}