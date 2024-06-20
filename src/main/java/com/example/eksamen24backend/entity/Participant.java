package com.example.eksamen24backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "participant")
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private String club;

    @ManyToMany
    @JoinTable(
            name = "participant_discipline",
            joinColumns = @JoinColumn(name = "participant_id"),
            inverseJoinColumns = @JoinColumn(name = "discipline_id")
    )
    private List<Discipline> disciplines;

    @OneToMany(mappedBy = "participant")
    private List<Result> results;

    public String getAgeGroup() {
        if (age >= 6 && age <= 9) {
            return "Children";
        } else if (age >= 10 && age <= 13) {
            return "Young";
        } else if (age >= 14 && age <= 22) {
            return "Junior";
        } else if (age >= 23 && age <= 40) {
            return "Adults";
        } else if (age >= 41) {
            return "Seniors";
        } else {
            return "Unknown";
        }
    }
}
