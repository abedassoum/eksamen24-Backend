package com.example.eksamen24backend.service;

import com.example.eksamen24backend.dto.DisciplineDto;
import com.example.eksamen24backend.entity.Discipline;
import com.example.eksamen24backend.repository.DisciplineRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DisciplineService {

    private final DisciplineRepository disciplineRepository;

    public DisciplineService(DisciplineRepository disciplineRepository) {
        this.disciplineRepository = disciplineRepository;
    }

    public List<DisciplineDto> getAllDisciplines() {
        List<Discipline> disciplines = disciplineRepository.findAll();
        return disciplines.stream().map(DisciplineDto::new).collect(Collectors.toList());
    }

    public DisciplineDto getDisciplineById(Integer id) {
        Discipline discipline = disciplineRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Discipline not found"));
        return new DisciplineDto(discipline);
    }

    public DisciplineDto addDiscipline(DisciplineDto request) {
        Discipline newDiscipline = new Discipline();
        updateDiscipline(newDiscipline, request);
        disciplineRepository.save(newDiscipline);
        return new DisciplineDto(newDiscipline);
    }

    public DisciplineDto editDiscipline(Integer id, DisciplineDto request) {
        if (!id.equals(request.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot change the ID of an existing discipline");
        }
        Discipline discipline = disciplineRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Discipline not found"));
        updateDiscipline(discipline, request);
        disciplineRepository.save(discipline);
        return new DisciplineDto(discipline);
    }

    private void updateDiscipline(Discipline discipline, DisciplineDto disciplineDto) {
        discipline.setName(disciplineDto.getName());
        discipline.setResultType(disciplineDto.getResultType());
    }
}
