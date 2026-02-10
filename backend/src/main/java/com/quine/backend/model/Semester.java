package com.quine.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name = "semesters")
public class Semester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int semNumber; // 1, 2, 3...

    @ManyToOne
    @JoinColumn(name = "program_id")
    private Program program;

    @OneToMany(mappedBy = "semester", cascade = CascadeType.ALL)
    @JsonIgnore // Infinite loop se bachne ke liye
    private List<Subject> subjects;
}