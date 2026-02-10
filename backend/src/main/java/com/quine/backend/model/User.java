package com.quine.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users") // Dhyan dena: 'users' likha hai kyunki 'user' database ka reserved word hota hai
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; // Student ID ya Employee ID yahan aayega

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING) // Role ko text (e.g. "STUDENT") ki tarah save karega
    private Role role;

    // Student ka data link karne ke liye (Optional)
    // Jab Student login karega, toh humein pata chal jayega ki wo kaunsa Student hai
    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}