package com.quine.backend.service;

import com.quine.backend.model.*;
import com.quine.backend.repository.ApplicationRepository;
import com.quine.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SemesterService semesterService;

    // 1. Registration Phase
    public Application applyNow(Application app) {
        app.setStatus("PENDING");
        return applicationRepository.save(app);
    }

    // 2. Automated Approval Phase (Ryu's Magic Logic 🔥)
    public Student approveApplication(Long applicationId) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application nahi mili!"));

        if (app.getStatus().equals("APPROVED")) {
            throw new RuntimeException("Bhai, ye pehle se approved hai!");
        }

        // --- THE MAGIC LOGIC ---
        // 1. Program ke liye actual Semester 1 search karo
        Semester sem1 = semesterService.findSemester(app.getProgram(), 1);

        // 2. Count current students in this specific semester
        long currentCount = studentRepository.countBySemester(sem1);
        int programCapacity = app.getProgram().getCapacity();

        if (currentCount >= programCapacity) {
            throw new RuntimeException("Bhai, " + app.getProgram().getName() + " ki seats full ho gayi hain!");
        }

        // 3. Assign Section based on count (Virtual Partitioning)
        int sectionSize = programCapacity / 4;
        SectionEnum section;
        if (currentCount < sectionSize) section = SectionEnum.ALPHA;
        else if (currentCount < sectionSize * 2) section = SectionEnum.BETA;
        else if (currentCount < sectionSize * 3) section = SectionEnum.GAMA;
        else section = SectionEnum.DELTA;

        // --- DATA TRANSFER ---
        Student newStudent = new Student();
        newStudent.setName(app.getName());
        newStudent.setEmail(app.getEmail());
        newStudent.setMobileNumber(app.getMobileNumber());
        newStudent.setFatherName(app.getFatherName());
        newStudent.setMotherName(app.getMotherName());

        // Link the searched objects
        newStudent.setSemester(sem1);
        newStudent.setAssignedSection(section);
        newStudent.setJoiningDate(LocalDateTime.now());

        // Status update
        app.setStatus("APPROVED");
        applicationRepository.save(app);

        return studentService.saveStudent(newStudent);
    }

    // 3. Get Pending Applications
    public List<Application> getPendingApplications() {
        return applicationRepository.findByStatus("PENDING");
    }
}