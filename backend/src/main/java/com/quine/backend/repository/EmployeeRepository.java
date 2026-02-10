package com.quine.backend.repository;

import com.quine.backend.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Last Employee ID dhoondho (taaki hum next bana sakein)
    @Query("SELECT MAX(e.employeeId) FROM Employee e")
    String findMaxEmployeeId();

    Employee findByEmployeeId(String employeeId);
}