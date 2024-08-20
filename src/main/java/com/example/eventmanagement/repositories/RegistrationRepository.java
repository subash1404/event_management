package com.example.eventmanagement.repositories;

import com.example.eventmanagement.models.Event;
import com.example.eventmanagement.models.Registration;
import com.example.eventmanagement.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    Registration findByStudentAndEvent(Student student, Event event);
}
