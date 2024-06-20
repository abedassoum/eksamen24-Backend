package com.example.eksamen24backend.dto;

import com.example.eksamen24backend.entity.Result;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultDto {
    private Integer id;
    private String resultType;
    private Date date;
    private String resultValue;
    private Integer disciplineId;
    private Integer participantId;

    public ResultDto(Result result) {
        this.id = result.getId();
        this.resultType = result.getResultType();
        this.date = result.getDate();
        this.resultValue = result.getResultValue();
        this.disciplineId = result.getDiscipline().getId();
        this.participantId = result.getParticipant().getId();
    }
}

