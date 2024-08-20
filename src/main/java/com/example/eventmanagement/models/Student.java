package com.example.eventmanagement.models;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name="student")
public class Student {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "college_id")
    private College college;

    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL)
    private List<Registration> registrations;

    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL)
    private List<Feedback> feedbacks ;
}
