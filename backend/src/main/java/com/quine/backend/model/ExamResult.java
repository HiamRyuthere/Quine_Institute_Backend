package com.quine.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "exam_results")
public class ExamResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Kiska result hai?
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    // Kis subject ka hai?
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    // Marks
    private Integer internalMarks; // Teacher ye edit karega
    private Integer externalMarks; // Exam Dept ye edit karega

    private String status; // PASS/FAIL (Hum code se calculate kar lenge)
    private int attempt; // 1
}