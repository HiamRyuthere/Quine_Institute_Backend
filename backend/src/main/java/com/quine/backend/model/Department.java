package com.quine.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //The Id of the department

    @Column(unique = true, nullable = false)
    private String name; //the name of department E.G COMPUTER SCIENCE

    @OneToOne //one employee can be HOD of one department only and vice versa
    @JoinColumn(name = "hod_id")
    private Employee hod; //This department will have one HOD ( employee )
}
