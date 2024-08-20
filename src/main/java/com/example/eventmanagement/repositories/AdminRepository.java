package com.example.eventmanagement.repositories;

import com.example.eventmanagement.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
