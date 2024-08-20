package com.example.eventmanagement.models;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name="registration")
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
    // CascaseType.PERSIST is not mentioned because i dont want new entries of students
    // or event to be saved when a registration is created
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(name = "registration_date")
    private LocalDate registrationDate;
}
