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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
public class ParticipantService {

    private static final Logger logger = LoggerFactory.getLogger(ParticipantService.class);

    private final ParticipantRepository participantRepository;
    private final DisciplineRepository disciplineRepository;
    private final ResultRepository resultRepository;

    public ParticipantService(ParticipantRepository participantRepository, DisciplineRepository disciplineRepository, ResultRepository resultRepository) {
        this.participantRepository = participantRepository;
        this.disciplineRepository = disciplineRepository;
        this.resultRepository = resultRepository;
    }

    public List<ParticipantDto> getAllParticipants() {
        try {
            List<Participant> participants = participantRepository.findAll();
            return participants.stream().map(this::mapToDto).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching all participants", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching all participants", e);
        }
    }

    public ParticipantDto getParticipantById(Integer id) {
        try {
            Participant participant = participantRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found"));
            return mapToDto(participant);
        } catch (Exception e) {
            logger.error("Error fetching participant by ID", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching participant by ID", e);
        }
    }

    public ParticipantDto addParticipant(ParticipantDto request) {
        try {
            Participant newParticipant = new Participant();
            updateParticipant(newParticipant, request);
            participantRepository.save(newParticipant);

            List<Result> resultsList = request.getResults() == null ?
                    new ArrayList<>() : request.getResults().stream().map(resultDto -> {
                Result result = new Result();
                result.setParticipant(newParticipant);
                Discipline discipline = disciplineRepository.findById(resultDto.getDisciplineId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Discipline not found"));
                result.setDiscipline(discipline);
                result.setResultType(resultDto.getResultType());
                result.setDate(resultDto.getDate());
                result.setResultValue(resultDto.getResultValue());
                return resultRepository.save(result);
            }).collect(Collectors.toList());

            newParticipant.setResults(resultsList);
            participantRepository.save(newParticipant);

            return mapToDto(newParticipant);
        } catch (Exception e) {
            logger.error("Error adding participant", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding participant", e);
        }
    }

    public ParticipantDto editParticipant(Integer id, ParticipantDto request) {
        try {
            if (!id.equals(request.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot change the ID of an existing participant");
            }
            Participant participant = participantRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found"));
            updateParticipant(participant, request);
            participantRepository.save(participant);
            return mapToDto(participant);
        } catch (Exception e) {
            logger.error("Error editing participant", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error editing participant", e);
        }
    }

    public void deleteParticipant(Integer id) {
        try {
            Participant participant = participantRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found"));
            participantRepository.delete(participant);
        } catch (Exception e) {
            logger.error("Error deleting participant", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting participant", e);
        }
    }

    private void updateParticipant(Participant participant, ParticipantDto participantDto) {
        participant.setName(participantDto.getName());
        participant.setGender(participantDto.getGender());
        participant.setAge(participantDto.getAge());
        participant.setClub(participantDto.getClub());

        List<Discipline> disciplines = participantDto.getDisciplines() == null ?
                new ArrayList<>() : participantDto.getDisciplines().stream().map(disciplineDto ->
                disciplineRepository.findById(disciplineDto.getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Discipline not found"))
        ).collect(Collectors.toList());

        participant.setDisciplines(disciplines);
    }

    public List<ParticipantDto> searchParticipantsByName(String name) {
        try {
            List<Participant> participants = participantRepository.findAll().stream()
                    .filter(participant -> participant.getName().contains(name))
                    .collect(Collectors.toList());
            return participants.stream().map(this::mapToDto).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error searching participants by name", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error searching participants by name", e);
        }
    }

    public List<ParticipantDto> listParticipants(String gender, Integer age, String club, String discipline) {
        try {
            return participantRepository.findAll().stream()
                    .filter(participant -> (gender == null || participant.getGender().equalsIgnoreCase(gender)) &&
                            (age == null || participant.getAge() == age) &&
                            (club == null || participant.getClub().equalsIgnoreCase(club)) &&
                            (discipline == null || participant.getDisciplines().stream().anyMatch(d -> d.getName().equalsIgnoreCase(discipline))))
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error listing participants", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error listing participants", e);
        }
    }

    private ParticipantDto mapToDto(Participant participant) {
        ParticipantDto dto = new ParticipantDto();
        dto.setId(participant.getId());
        dto.setName(participant.getName());
        dto.setGender(participant.getGender());
        dto.setAge(participant.getAge());
        dto.setClub(participant.getClub());
        dto.setDisciplines(participant.getDisciplines() != null ? participant.getDisciplines().stream().map(discipline -> {
            DisciplineDto disciplineDto = new DisciplineDto();
            disciplineDto.setId(discipline.getId());
            disciplineDto.setName(discipline.getName());
            disciplineDto.setResultType(discipline.getResultType());
            return disciplineDto;
        }).collect(Collectors.toList()) : new ArrayList<>());
        dto.setResults(participant.getResults() != null ? participant.getResults().stream().map(result -> {
            ResultDto resultDto = new ResultDto(result);
            return resultDto;
        }).collect(Collectors.toList()) : new ArrayList<>());
        return dto;
    }
}
