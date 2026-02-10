package com.quine.backend.repository;

import com.quine.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Login ke liye username se user dhoondhna padega
    Optional<User> findByUsername(String username);
}