package com.example.eventmanagement.repositories;

import com.example.eventmanagement.models.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
