package com.example.eksamen24backend.service;

import com.example.eksamen24backend.dto.ResultDto;
import com.example.eksamen24backend.entity.Discipline;
import com.example.eksamen24backend.entity.Participant;
import com.example.eksamen24backend.entity.Result;
import com.example.eksamen24backend.repository.DisciplineRepository;
import com.example.eksamen24backend.repository.ParticipantRepository;
import com.example.eksamen24backend.repository.ResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResultService {

    private static final Logger logger = LoggerFactory.getLogger(ResultService.class);

    private final ResultRepository resultRepository;
    private final DisciplineRepository disciplineRepository;
    private final ParticipantRepository participantRepository;

    public ResultService(ResultRepository resultRepository, DisciplineRepository disciplineRepository, ParticipantRepository participantRepository) {
        this.resultRepository = resultRepository;
        this.disciplineRepository = disciplineRepository;
        this.participantRepository = participantRepository;
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
        try {
            Result newResult = new Result();
            updateResult(newResult, request);
            Discipline discipline = disciplineRepository.findById(request.getDisciplineId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Discipline not found"));
            Participant participant = participantRepository.findById(request.getParticipantId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found"));
            newResult.setDiscipline(discipline);
            newResult.setParticipant(participant);
            resultRepository.save(newResult);
            return new ResultDto(newResult);
        } catch (Exception e) {
            logger.error("Error adding result", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding result", e);
        }
    }

    public ResultDto editResult(Integer id, ResultDto request) {
        try {
            if (!id.equals(request.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot change the ID of an existing result");
            }
            Result result = resultRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Result not found"));
            updateResult(result, request);
            Discipline discipline = disciplineRepository.findById(request.getDisciplineId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Discipline not found"));
            Participant participant = participantRepository.findById(request.getParticipantId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found"));
            result.setDiscipline(discipline);
            result.setParticipant(participant);
            resultRepository.save(result);
            return new ResultDto(result);
        } catch (Exception e) {
            logger.error("Error editing result", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error editing result", e);
        }
    }

    public void deleteResult(Integer id) {
        try {
            Result result = resultRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Result not found"));
            resultRepository.delete(result);
        } catch (Exception e) {
            logger.error("Error deleting result", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting result", e);
        }
    }

    private void updateResult(Result result, ResultDto resultDto) {
        result.setResultType(resultDto.getResultType());
        result.setDate(resultDto.getDate());
        result.setResultValue(resultDto.getResultValue());
        if (resultDto.getDisciplineId() != null) {
            Discipline discipline = disciplineRepository.findById(resultDto.getDisciplineId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Discipline not found"));
            result.setDiscipline(discipline);
        }
        if (resultDto.getParticipantId() != null) {
            Participant participant = participantRepository.findById(resultDto.getParticipantId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found"));
            result.setParticipant(participant);
        }
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
