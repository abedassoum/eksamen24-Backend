package com.example.eksamen24backend.service;

import com.example.eksamen24backend.dto.DisciplineDto;
import com.example.eksamen24backend.entity.Discipline;
import com.example.eksamen24backend.repository.DisciplineRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DisciplineServiceTest {

    @Mock
    private DisciplineRepository disciplineRepository;

    @InjectMocks
    private DisciplineService disciplineService;

    @Test
    void getAllDisciplines() {

        Discipline discipline = new Discipline();
        discipline.setId(1);

        when(disciplineRepository.findAll()).thenReturn(List.of(discipline));


        List<DisciplineDto> disciplineDtos = disciplineService.getAllDisciplines();


        assertEquals(1, disciplineDtos.size());
        verify(disciplineRepository, times(1)).findAll();
    }

    @Test
    void getDisciplineById() {

        Discipline discipline = new Discipline();
        discipline.setId(1);

        when(disciplineRepository.findById(1)).thenReturn(Optional.of(discipline));


        DisciplineDto disciplineDto = disciplineService.getDisciplineById(1);


        assertNotNull(disciplineDto);
        assertEquals(1, disciplineDto.getId());
        verify(disciplineRepository, times(1)).findById(1);
    }

    @Test
    void getDisciplineByIdNotFound() {

        when(disciplineRepository.findById(1)).thenReturn(Optional.empty());


        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> disciplineService.getDisciplineById(1));
        assertEquals("404 NOT_FOUND \"Discipline not found\"", exception.getMessage());
        verify(disciplineRepository, times(1)).findById(1);
    }

    @Test
    void addDiscipline() {

        DisciplineDto disciplineDto = new DisciplineDto();
        disciplineDto.setName("Discipline Name");
        disciplineDto.setResultType("Time");

        Discipline discipline = new Discipline();
        discipline.setId(1);

        when(disciplineRepository.save(any(Discipline.class))).thenAnswer(i -> {
            Discipline savedDiscipline = (Discipline) i.getArguments()[0];
            savedDiscipline.setId(1);
            return savedDiscipline;
        });


        DisciplineDto createdDiscipline = disciplineService.addDiscipline(disciplineDto);


        assertNotNull(createdDiscipline);
        assertEquals(disciplineDto.getName(), createdDiscipline.getName());
        verify(disciplineRepository, times(1)).save(any(Discipline.class));
    }

    @Test
    void editDiscipline() {

        DisciplineDto disciplineDto = new DisciplineDto();
        disciplineDto.setId(1);
        disciplineDto.setName("Updated Discipline");
        disciplineDto.setResultType("Time");

        Discipline existingDiscipline = new Discipline();
        existingDiscipline.setId(1);

        when(disciplineRepository.findById(1)).thenReturn(Optional.of(existingDiscipline));
        when(disciplineRepository.save(any(Discipline.class))).thenAnswer(i -> i.getArguments()[0]);


        DisciplineDto updatedDiscipline = disciplineService.editDiscipline(1, disciplineDto);


        assertNotNull(updatedDiscipline);
        assertEquals(disciplineDto.getName(), updatedDiscipline.getName());
        verify(disciplineRepository, times(1)).findById(1);
        verify(disciplineRepository, times(1)).save(any(Discipline.class));
    }

    @Test
    void editDisciplineNotFound() {

        DisciplineDto disciplineDto = new DisciplineDto();
        disciplineDto.setId(1);

        when(disciplineRepository.findById(1)).thenReturn(Optional.empty());


        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> disciplineService.editDiscipline(1, disciplineDto));
        assertEquals("404 NOT_FOUND \"Discipline not found\"", exception.getMessage());
        verify(disciplineRepository, times(1)).findById(1);
        verify(disciplineRepository, never()).save(any(Discipline.class));
    }

    @Test
    void deleteDiscipline() {

        Discipline discipline = new Discipline();
        discipline.setId(1);

        when(disciplineRepository.findById(1)).thenReturn(Optional.of(discipline));


        disciplineService.deleteDiscipline(1);

        verify(disciplineRepository, times(1)).delete(discipline);
    }

    @Test
    void deleteDisciplineNotFound() {

        when(disciplineRepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> disciplineService.deleteDiscipline(1));
        assertEquals("404 NOT_FOUND \"Discipline not found\"", exception.getMessage());
        verify(disciplineRepository, times(1)).findById(1);
        verify(disciplineRepository, never()).delete(any(Discipline.class));
    }
}
