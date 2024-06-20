package com.example.eksamen24backend.service;

import com.example.eksamen24backend.dto.ParticipantDto;
import com.example.eksamen24backend.entity.Participant;
import com.example.eksamen24backend.repository.ParticipantRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;


    public ParticipantService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public List<ParticipantDto> getAllParticipants() {
        List<Participant> participants = participantRepository.findAll();
        return participants.stream().map(ParticipantDto::new).collect(Collectors.toList());
    }

    public ParticipantDto getParticipantById(Integer id) {
        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found"));
        return new ParticipantDto(participant);
    }

    public ParticipantDto addParticipant(ParticipantDto request) {
        Participant newParticipant = new Participant();
        updateParticipant(newParticipant, request);
        participantRepository.save(newParticipant);
        return new ParticipantDto(newParticipant);
    }

    public ParticipantDto editParticipant(Integer id, ParticipantDto request) {
        if (!id.equals(request.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot change the ID of an existing participant");
        }
        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found"));
        updateParticipant(participant, request);
        participantRepository.save(participant);
        return new ParticipantDto(participant);
    }

    public void deleteParticipant(Integer id) {
        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found"));
        participantRepository.delete(participant);
    }

    private void updateParticipant(Participant participant, ParticipantDto participantDto) {
        participant.setName(participantDto.getName());
        participant.setGender(participantDto.getGender());
        participant.setAge(participantDto.getAge());
        participant.setClub(participantDto.getClub());

    }

    public List<ParticipantDto> searchParticipantsByName(String name) {
        List<Participant> participants = participantRepository.findByName(name);
        return participants.stream().map(ParticipantDto::new).collect(Collectors.toList());
    }

    public List<ParticipantDto> listParticipants(String gender, Integer age, String club, String discipline) {

        return participantRepository.findAll().stream()
                .filter(participant -> (gender == null || participant.getGender().equalsIgnoreCase(gender)) &&
                        (age == null || participant.getAge() == age) &&
                        (club == null || participant.getClub().equalsIgnoreCase(club)))
                .map(ParticipantDto::new)
                .collect(Collectors.toList());
    }
}
