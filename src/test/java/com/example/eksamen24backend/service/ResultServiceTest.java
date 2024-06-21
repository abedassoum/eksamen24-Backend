package com.example.eksamen24backend.service;

import com.example.eksamen24backend.dto.ResultDto;
import com.example.eksamen24backend.entity.Discipline;
import com.example.eksamen24backend.entity.Participant;
import com.example.eksamen24backend.entity.Result;
import com.example.eksamen24backend.repository.DisciplineRepository;
import com.example.eksamen24backend.repository.ParticipantRepository;
import com.example.eksamen24backend.repository.ResultRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResultServiceTest {

    @Mock
    private ResultRepository resultRepository;

    @Mock
    private DisciplineRepository disciplineRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @InjectMocks
    private ResultService resultService;

    @Test
    void getAllResults() {
        // Arrange
        Result result = new Result();
        result.setId(1);
        Discipline discipline = new Discipline();
        discipline.setId(1);
        Participant participant = new Participant();
        participant.setId(1);
        result.setDiscipline(discipline);
        result.setParticipant(participant);

        when(resultRepository.findAll()).thenReturn(List.of(result));

        List<ResultDto> resultDtos = resultService.getAllResults();
        assertEquals(1, resultDtos.size());
        verify(resultRepository, times(1)).findAll();
    }

    @Test
    void getResultById() {
        // Arrange
        Result result = new Result();
        result.setId(1);
        Discipline discipline = new Discipline();
        discipline.setId(1);
        Participant participant = new Participant();
        participant.setId(1);
        result.setDiscipline(discipline);
        result.setParticipant(participant);

        when(resultRepository.findById(1)).thenReturn(Optional.of(result));
        ResultDto resultDto = resultService.getResultById(1);
        assertNotNull(resultDto);
        assertEquals(1, resultDto.getId());
        verify(resultRepository, times(1)).findById(1);
    }

    @Test
    void getResultByIdNotFound() {

        when(resultRepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> resultService.getResultById(1));
        assertEquals("404 NOT_FOUND \"Result not found\"", exception.getMessage());
        verify(resultRepository, times(1)).findById(1);
    }

    @Test
    void addResult() {

        ResultDto resultDto = new ResultDto();
        resultDto.setDisciplineId(1);
        resultDto.setParticipantId(1);
        resultDto.setResultType("Time");

        LocalDateTime localDateTime = LocalDateTime.parse("2024-06-20T14:30:00");
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        resultDto.setDate(date);

        resultDto.setResultValue("10.5");

        Discipline discipline = new Discipline();
        discipline.setId(1);

        Participant participant = new Participant();
        participant.setId(1);

        when(disciplineRepository.findById(1)).thenReturn(Optional.of(discipline));
        when(participantRepository.findById(1)).thenReturn(Optional.of(participant));
        when(resultRepository.save(any(Result.class))).thenAnswer(i -> i.getArguments()[0]);

        ResultDto createdResult = resultService.addResult(resultDto);

        assertNotNull(createdResult);
        assertEquals(resultDto.getResultType(), createdResult.getResultType());
        verify(resultRepository, times(1)).save(any(Result.class));
    }


    @Test
    void editResult() {

        ResultDto resultDto = new ResultDto();
        resultDto.setId(1);
        resultDto.setDisciplineId(1);
        resultDto.setParticipantId(1);
        resultDto.setResultType("Time");

        LocalDateTime localDateTime = LocalDateTime.parse("2024-06-20T14:30:00");
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        resultDto.setDate(date);

        resultDto.setResultValue("10.5");

        Result existingResult = new Result();
        existingResult.setId(1);
        existingResult.setDiscipline(new Discipline());
        existingResult.setParticipant(new Participant());

        Discipline discipline = new Discipline();
        discipline.setId(1);

        Participant participant = new Participant();
        participant.setId(1);

        when(resultRepository.findById(1)).thenReturn(Optional.of(existingResult));
        when(disciplineRepository.findById(1)).thenReturn(Optional.of(discipline));
        when(participantRepository.findById(1)).thenReturn(Optional.of(participant));
        when(resultRepository.save(any(Result.class))).thenAnswer(i -> i.getArguments()[0]);

        ResultDto updatedResult = resultService.editResult(1, resultDto);
        assertNotNull(updatedResult);
        assertEquals(resultDto.getResultType(), updatedResult.getResultType());
        verify(resultRepository, times(1)).save(any(Result.class));
    }


    @Test
    void deleteResult() {

        Result result = new Result();
        result.setId(1);
        when(resultRepository.findById(1)).thenReturn(Optional.of(result));
        resultService.deleteResult(1);
        verify(resultRepository, times(1)).delete(result);
    }

}
