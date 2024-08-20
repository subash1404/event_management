package com.example.eventmanagement.repositories;

import com.example.eventmanagement.enums.EventType;
import com.example.eventmanagement.enums.Type;
import com.example.eventmanagement.models.College;
import com.example.eventmanagement.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {
    Set<Event> findByCollege(College college);
    Set<Event> findByLocation(String location);
    Set<Event> findByDate(LocalDate date);
    Set<Event> findByName(String name);
    Set<Event> findByType(Type type);
    Set<Event> findByEventType(EventType eventType);
    Optional<Event> findByCollegeAndName(College college, String name);
    void deleteByCollegeAndName(College college, String name);
}
