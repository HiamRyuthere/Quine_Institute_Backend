package com.quine.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "noticeArchives")
public class NoticeArchive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 5000)
    private String content;

    private String type;

    private LocalDate date;
    private LocalDate dateOfExecution;

    // Archive mein 'isNew' ki zarurat nahi hai
}