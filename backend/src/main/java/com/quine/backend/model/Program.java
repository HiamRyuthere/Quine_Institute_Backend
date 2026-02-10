package com.quine.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "programs")
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; // BCA

    private String fullName;
    private Integer capacity; // e.g., 240
    private int durationYears;
    // Program.java mein
    @Column(name = "annual_fee", nullable = false)
    private Double annualFee = 0.0; // Default value set kar di

    @Column(name = "total_semesters") // Database mein jo column hai wahi naam do
    private Integer totalSemesters;
}