package com.example.eventmanagement.repositories;

import com.example.eventmanagement.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
