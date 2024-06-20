package com.example.eksamen24backend.service;

import com.example.eksamen24backend.dto.ResultDto;
import com.example.eksamen24backend.entity.Discipline;
import com.example.eksamen24backend.entity.Result;
import com.example.eksamen24backend.repository.DisciplineRepository;
import com.example.eksamen24backend.repository.ResultRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResultService {

    private final ResultRepository resultRepository;
    private final DisciplineRepository disciplineRepository;

    public ResultService(ResultRepository resultRepository, DisciplineRepository disciplineRepository) {
        this.resultRepository = resultRepository;
        this.disciplineRepository = disciplineRepository;
    }

    public List<ResultDto> getAllResults() {
        List<Result> results = resultRepository.findAll();
        return results.stream().map(ResultDto::new).collect(Collectors.toList());
    }

    public ResultDto getResultById(Integer id) {
        Result result = resultRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Result not found"));
        return new ResultDto(result);
    }

    public ResultDto addResult(ResultDto request) {
        Result newResult = new Result();
        updateResult(newResult, request);
        Discipline discipline = disciplineRepository.findById(request.getDisciplineId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Discipline not found"));
        newResult.setDiscipline(discipline);
        resultRepository.save(newResult);
        return new ResultDto(newResult);
    }

    public ResultDto editResult(Integer id, ResultDto request) {
        if (!id.equals(request.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot change the ID of an existing result");
        }
        Result result = resultRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Result not found"));
        updateResult(result, request);
        resultRepository.save(result);
        return new ResultDto(result);
    }

    public void deleteResult(Integer id) {
        Result result = resultRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Result not found"));
        resultRepository.delete(result);
    }

    private void updateResult(Result result, ResultDto resultDto) {
        result.setResultType(resultDto.getResultType());
        result.setDate(resultDto.getDate());
        result.setResultValue(resultDto.getResultValue());
        // You may need to handle Participant and Discipline relationships here as well
    }

    public List<ResultDto> filterResults(String discipline, String gender, String ageGroup) {
        return resultRepository.findAll().stream()
                .filter(result -> (discipline == null || result.getDiscipline().getName().equalsIgnoreCase(discipline)) &&
                        (gender == null || result.getParticipant().getGender().equalsIgnoreCase(gender)) &&
                        (ageGroup == null || result.getParticipant().getAgeGroup().equalsIgnoreCase(ageGroup)))
                .map(ResultDto::new)
                .collect(Collectors.toList());
    }
}
