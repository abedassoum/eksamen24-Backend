package com.example.eksamen24backend.api;

import com.example.eksamen24backend.dto.DisciplineDto;
import com.example.eksamen24backend.service.DisciplineService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disciplines")
public class DisciplineController {

    private final DisciplineService disciplineService;

    public DisciplineController(DisciplineService disciplineService) {
        this.disciplineService = disciplineService;
    }

    @PostMapping
    public DisciplineDto addDiscipline(@RequestBody DisciplineDto request) {
        return disciplineService.addDiscipline(request);
    }

    @GetMapping
    public List<DisciplineDto> getAllDisciplines() {
        return disciplineService.getAllDisciplines();
    }

    @GetMapping("/{id}")
    public DisciplineDto getDisciplineById(@PathVariable Integer id) {
        return disciplineService.getDisciplineById(id);
    }

    @PutMapping("/{id}")
    public DisciplineDto editDiscipline(@PathVariable Integer id, @RequestBody DisciplineDto request) {
        return disciplineService.editDiscipline(id, request);
    }
}

