package com.example.eksamen24backend.service;

import com.example.eksamen24backend.dto.DisciplineDto;
import com.example.eksamen24backend.dto.ParticipantDto;
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


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceTest {

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private DisciplineRepository disciplineRepository;

    @Mock
    private ResultRepository resultRepository;

    @InjectMocks
    private ParticipantService participantService;

    @Test
    void getAllParticipants() {
        Participant participant = new Participant();
        participant.setId(1);

        when(participantRepository.findAll()).thenReturn(List.of(participant));

        List<ParticipantDto> participantDtos = participantService.getAllParticipants();

        assertEquals(1, participantDtos.size());
        verify(participantRepository, times(1)).findAll();
    }

    @Test
    void getParticipantById() {
        Participant participant = new Participant();
        participant.setId(1);

        when(participantRepository.findById(1)).thenReturn(Optional.of(participant));

        ParticipantDto participantDto = participantService.getParticipantById(1);

        assertNotNull(participantDto);
        assertEquals(1, participantDto.getId());
        verify(participantRepository, times(1)).findById(1);
    }

    @Test
    void addParticipant() {
        ParticipantDto participantDto = new ParticipantDto();
        participantDto.setName("John Doe");

        Participant participant = new Participant();
        participant.setId(1);

        when(participantRepository.save(any(Participant.class))).thenAnswer(i -> {
            Participant savedParticipant = (Participant) i.getArguments()[0];
            savedParticipant.setId(1);
            return savedParticipant;
        });

        Discipline discipline = new Discipline();
        discipline.setId(1);

        ResultDto resultDto = new ResultDto();
        resultDto.setDisciplineId(1);

        Result result = new Result();
        result.setParticipant(participant);
        result.setDiscipline(discipline);

        participantDto.setResults(List.of(resultDto));

        when(disciplineRepository.findById(1)).thenReturn(Optional.of(discipline));
        when(resultRepository.save(any(Result.class))).thenAnswer(i -> i.getArguments()[0]);

        ParticipantDto createdParticipant = participantService.addParticipant(participantDto);

        assertNotNull(createdParticipant);
        assertEquals(participantDto.getName(), createdParticipant.getName());
        verify(participantRepository, times(2)).save(any(Participant.class));
    }

    @Test
    void editParticipant() {
        ParticipantDto participantDto = new ParticipantDto();
        participantDto.setId(1);
        participantDto.setName("John Doe");

        Participant participant = new Participant();
        participant.setId(1);

        Discipline discipline = new Discipline();
        discipline.setId(1);

        ResultDto resultDto = new ResultDto();
        resultDto.setDisciplineId(1);

        participantDto.setResults(List.of(resultDto));

        when(participantRepository.findById(1)).thenReturn(Optional.of(participant));
        when(disciplineRepository.findById(1)).thenReturn(Optional.of(discipline));
        when(resultRepository.save(any(Result.class))).thenAnswer(i -> i.getArguments()[0]);

        ParticipantDto updatedParticipant = participantService.editParticipant(1, participantDto);

        assertNotNull(updatedParticipant);
        assertEquals(participantDto.getName(), updatedParticipant.getName());
        verify(participantRepository, times(2)).save(any(Participant.class));
    }

    @Test
    void deleteParticipant() {
        Participant participant = new Participant();
        participant.setId(1);

        when(participantRepository.findById(1)).thenReturn(Optional.of(participant));

        participantService.deleteParticipant(1);

        verify(participantRepository, times(1)).delete(participant);
        verify(resultRepository, times(1)).deleteAll(participant.getResults());
    }

    @Test
    void searchParticipantsByName() {
        Participant participant = new Participant();
        participant.setName("John Doe");

        when(participantRepository.findAll()).thenReturn(List.of(participant));

        List<ParticipantDto> participantDtos = participantService.searchParticipantsByName("John");

        assertEquals(1, participantDtos.size());
        verify(participantRepository, times(1)).findAll();
    }

    @Test
    void listParticipants() {
        Participant participant = new Participant();
        participant.setGender("Male");
        participant.setAge(25);
        participant.setClub("ABC Club");

        Discipline discipline = new Discipline();
        discipline.setName("Running");

        participant.setDisciplines(List.of(discipline));

        when(participantRepository.findAll()).thenReturn(List.of(participant));

        List<ParticipantDto> participantDtos = participantService.listParticipants("Male", 25, "ABC Club", "Running");

        assertEquals(1, participantDtos.size());
        verify(participantRepository, times(1)).findAll();
    }
}
