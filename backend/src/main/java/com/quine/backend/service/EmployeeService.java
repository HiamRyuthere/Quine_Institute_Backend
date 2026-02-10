package com.quine.backend.service;

import com.quine.backend.model.Employee;
import com.quine.backend.model.Role;
import com.quine.backend.model.User;
import com.quine.backend.repository.EmployeeRepository;
import com.quine.backend.repository.UserRepository; // New Import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository; // New

    public Employee addEmployee(Employee employee) {

        // 1. Employee ID Logic
        String maxId = employeeRepository.findMaxEmployeeId();
        int nextIdNum = 1;

        if (maxId != null) {
            nextIdNum = Integer.parseInt(maxId.replace("EMP", "")) + 1;
        }

        String empId = String.format("EMP%03d", nextIdNum);
        employee.setEmployeeId(empId);

        // 2. Employee Save
        Employee savedEmployee = employeeRepository.save(employee);

        // 3. AUTOMATIC USER CREATION LOGIC
        User newUser = new User();
        newUser.setUsername(empId);     // Username = EMP001
        newUser.setPassword(passwordEncoder.encode(employee.getMobileNumber())); // Default Password

        // ASSIGNING ROLE'S PROPERLY TO STAFF, IS ROLE ISN'T DEFINE, ROLE IS STAFF(SECURITY GUARD,DATA ENTERY GUY ETC)
        if ("TEACHER".equalsIgnoreCase(employee.getProfession())) {
            newUser.setRole(Role.FACULTY);
        }
        else if ("ADMIN".equalsIgnoreCase(employee.getProfession())) {
            newUser.setRole(Role.ADMIN);
        }
        else if("EXAMINER".equalsIgnoreCase(employee.getProfession())){
            newUser.setRole(Role.EXAMINER);
        }
        else {
            newUser.setRole(Role.STAFF);
        }

        // Note: Employee Entity me humne "User" ka relation nahi banaya tha,
        // agar zaroorat padi to baad me jod denge. Abhi bas login ban raha hai.

        newUser.setEmployee(savedEmployee);
        userRepository.save(newUser);

        return savedEmployee;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee findEmpById(String empId ){
        return employeeRepository.findByEmployeeId(empId);
    }
}