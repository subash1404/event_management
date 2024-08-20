package com.example.eventmanagement.repositories;

import com.example.eventmanagement.models.College;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollegeRepository extends JpaRepository<College, Long> {
}
