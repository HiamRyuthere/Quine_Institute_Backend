package com.quine.backend.repository;

import com.quine.backend.model.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {
    // Ek student ke saare results nikaalne ke liye
    List<ExamResult> findByStudentId(Long studentId);
}