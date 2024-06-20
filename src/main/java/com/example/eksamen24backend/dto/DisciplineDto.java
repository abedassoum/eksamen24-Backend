package com.example.eksamen24backend.dto;

import com.example.eksamen24backend.entity.Discipline;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DisciplineDto {
    private Integer id;
    private String name;
    private String resultType;

    public DisciplineDto(Discipline discipline) {
        this.id = discipline.getId();
        this.name = discipline.getName();
        this.resultType = discipline.getResultType();
    }
}

