package com.quine.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 10)
    private String studentId;

    private String FatherName;
    private String motherName;
    private String name;
    private String email;
    private String mobileNumber;

    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester; // Link to Semester (which links to Program)

    @Enumerated(EnumType.STRING)
    private SectionEnum assignedSection;

    private LocalDateTime joiningDate = LocalDateTime.now();

    //**TESTING FEES**
    private Double totalFees = 50000.0;
    private Double feesPaid = 0.0;
}