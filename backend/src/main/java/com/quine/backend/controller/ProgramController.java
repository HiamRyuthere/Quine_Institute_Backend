package com.quine.backend.controller;

import com.quine.backend.model.Program;
import com.quine.backend.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/programs")
@CrossOrigin(origins = "*")
public class ProgramController {

    @Autowired
    private ProgramService programService;

    @PostMapping
    public Program addProgram(@RequestBody Program program) {
        return programService.addProgram(program);
    }

    @GetMapping
    public List<Program> getAllPrograms() {
        return programService.getAllPrograms();
    }
}