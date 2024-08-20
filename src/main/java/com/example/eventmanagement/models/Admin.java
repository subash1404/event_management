package com.example.eventmanagement.models;

import javax.persistence.*;

import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name="admin")
public class Admin {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @OneToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.PERSIST})
    @JoinColumn(name="college_id")
    private College college;

    @OneToMany(mappedBy = "admin",cascade = {CascadeType.ALL})
    private List<Event> events;
}