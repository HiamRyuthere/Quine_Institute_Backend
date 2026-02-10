package com.quine.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="employees")
public class Employee {

    //identification

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //id details
    @Column (nullable = false, unique = true)
    private String employeeId;
    @Column(nullable = false)
    private String mobileNumber;

    //personal details
    private String email;
    private String name;
    private Double salary;

    //profesional details
    private String profession; //TEACHER, HOD , SECURITY, STAFF
    private String department; //CS, PHYSICS,

    //other details for auth

    private String assignProgram;
    private String assignSemester;
    private String assignSection;


}
