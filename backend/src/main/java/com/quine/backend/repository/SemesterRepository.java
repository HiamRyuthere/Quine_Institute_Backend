package com.quine.backend.repository;

import com.quine.backend.model.Semester;
import com.quine.backend.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {
    // Specific Program aur Semester number ke hisab se dhoondhne ke liye
    Optional<Semester> findByProgramAndSemNumber(Program program, int semNumber);

    List<Semester> findAllByProgram(Program program);
}