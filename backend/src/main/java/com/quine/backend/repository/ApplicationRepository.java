package com.quine.backend.repository;

import com.quine.backend.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    // Status ke basis pe applications dhoondhne ke liye
    List<Application> findByStatus(String status);
}