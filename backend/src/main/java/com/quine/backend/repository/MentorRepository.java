package com.quine.backend.repository;

import com.quine.backend.model.Mentor;
import com.quine.backend.model.Semester;
import com.quine.backend.model.SectionEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MentorRepository extends JpaRepository<Mentor, Long> {

    // Student ka Semester aur Section pata ho toh Mentor dhoondhne ke liye [cite: 2025-06-23]
    Optional<Mentor> findBySemesterAndSection(Semester semester, SectionEnum section);
}