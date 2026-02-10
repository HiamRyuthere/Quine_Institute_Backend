package com.quine.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "subjects")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subjectName; // e.g., "Java Programming"
    private String subjectCode; // e.g., "CS101"

    // Major, Minor, Open Elective
    private String type;

    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester; // Ab ye actual object se link hai
}