package com.quine.backend.repository;

import com.quine.backend.model.Subject;
import com.quine.backend.model.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    // Rasta: Subject ke andar Semester dhoondho, uske andar Program, aur uski ID match karo
    List<Subject> findBySemester_Program_Id(Long programId);

    // Specific Semester ke subjects nikalne ke liye
    List<Subject> findBySemester(Semester semester);
}