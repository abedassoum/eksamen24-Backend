package com.example.eksamen24backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "discipline")
public class Discipline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String resultType; 

    @ManyToMany(mappedBy = "disciplines")
    private List<Participant> participants;

    @OneToMany(mappedBy = "discipline")
    private List<Result> results;
}

