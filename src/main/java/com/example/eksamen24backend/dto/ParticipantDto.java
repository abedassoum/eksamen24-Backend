package com.example.eksamen24backend.dto;

import com.example.eksamen24backend.entity.Participant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ParticipantDto {
    private Integer id;
    private String name;
    private String gender;
    private int age;
    private String club;


    public ParticipantDto(Participant participant) {
        this.id = participant.getId();
        this.name = participant.getName();
        this.gender = participant.getGender();
        this.age = participant.getAge();
        this.club = participant.getClub();

    }
}

