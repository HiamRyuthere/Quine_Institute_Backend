package com.quine.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name="notices")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //this will be id of the particular notice

    private String title;

    @Column(length = 5000)
    private String content;

    private String type;
    //the type of event ("Examination, general, event)

    private LocalDate date;//date when notice was issues
    private LocalDate dateOfExecution; //The date of actual event

    private boolean isNew;


}
