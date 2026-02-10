package com.quine.backend.service;

import com.quine.backend.model.Role;
import com.quine.backend.model.Student;
import com.quine.backend.model.User;
import com.quine.backend.repository.StudentRepository;
import com.quine.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public Student saveStudent(Student student) {

        // 1. Student ID Auto-Generate Logic
        String maxId = studentRepository.findMaxStudentId();
        int nextIdNumber = 100001;

        if (maxId != null) {
            nextIdNumber = Integer.parseInt(maxId) + 1;
        }

        if (nextIdNumber > 999999) {
            throw new RuntimeException("Student Id limit reached");
        }

        String studentId = String.valueOf(nextIdNumber);
        student.setStudentId(studentId);

        // 2. Joining Date set karna (agar frontend se nahi aaya)
        if (student.getJoiningDate() == null) {
            student.setJoiningDate(LocalDateTime.now());
        }

        // 3. Default Fees set karna (agar nahi diye)
        if (student.getTotalFees() == null) {
            student.setTotalFees(50000.0);
        }
        if (student.getFeesPaid() == null) {
            student.setFeesPaid(0.0);
        }

        // 4. Student Save karo
        Student savedStudent = studentRepository.save(student);

        // 5. AUTOMATIC USER CREATION LOGIC
        User newUser = new User();
        newUser.setUsername(studentId); // Username = Student ID
        newUser.setPassword(passwordEncoder.encode(student.getMobileNumber())); // Default Password = Mobile No
        newUser.setRole(Role.STUDENT);
        newUser.setStudent(savedStudent); // Student se link kar diya

        userRepository.save(newUser); // User table me entry ho gayi

        return savedStudent;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Roll No se Student laane ke liye
    public Optional<Student> getStudentByRollNo(String rollNo) {
        return studentRepository.findByStudentId(rollNo);
    }
}