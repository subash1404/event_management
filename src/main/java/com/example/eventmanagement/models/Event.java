package com.example.eventmanagement.models;

import com.example.eventmanagement.enums.EventType;
import com.example.eventmanagement.enums.Type;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "location")
    private String location;

    @Column(name = "max_registration_limit")
    private Integer maxRegistrationLimit;

    @Column(name = "current_registrations")
    private Integer currentRegistrations = 0;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.PERSIST})
    @JoinColumn(name = "college_id")
    private College college;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.PERSIST})
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "event_type")
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL)
    private List<Registration> registrations;

    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL)
    private List<Feedback> feedbacks ;
}
