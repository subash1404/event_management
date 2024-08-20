package com.example.eventmanagement.models;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name="college")
public class College {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    @OneToOne(cascade = CascadeType.ALL,mappedBy = "college")
    private Admin admin;

    @OneToMany(mappedBy = "college",cascade = CascadeType.ALL)
    private List<Student> students;

    @OneToMany(mappedBy = "college",cascade = CascadeType.ALL)
    private List<Event> events;

}
