package com.quine.backend.repository;

import com.quine.backend.model.Semester;
import com.quine.backend.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional; // Import add karna

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT MAX(s.studentId) FROM Student s")
    String findMaxStudentId();

    //Roll No (Student ID) se dhoondhne ke liye
    Optional<Student> findByStudentId(String studentId);

    long countBySemester(Semester semester);
}