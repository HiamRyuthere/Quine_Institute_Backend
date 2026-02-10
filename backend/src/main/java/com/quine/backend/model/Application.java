package com.quine.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Applicant Details
    private String name;
    private String fatherName;
    private String motherName;
    private String email;
    private String mobileNumber;

    // Kis course ke liye apply kiya?
    @ManyToOne
    @JoinColumn(name = "program_id")
    private Program program; // e.g. BCA

    // 12th ke marks (Criteria check ke liye)
    private double percentage;

    // Status: PENDING, APPROVED, REJECTED
    private String status;
}