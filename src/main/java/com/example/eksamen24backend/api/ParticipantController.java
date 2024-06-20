package com.example.eksamen24backend.api;

import com.example.eksamen24backend.dto.ParticipantDto;
import com.example.eksamen24backend.service.ParticipantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

    private final ParticipantService participantService;

    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @PostMapping
    public ParticipantDto addParticipant(@RequestBody ParticipantDto request) {
        return participantService.addParticipant(request);
    }

    @GetMapping
    public List<ParticipantDto> getAllParticipants() {
        return participantService.getAllParticipants();
    }

    @GetMapping("/{id}")
    public ParticipantDto getParticipantById(@PathVariable Integer id) {
        return participantService.getParticipantById(id);
    }

    @PutMapping("/{id}")
    public ParticipantDto editParticipant(@PathVariable Integer id, @RequestBody ParticipantDto request) {
        return participantService.editParticipant(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteParticipant(@PathVariable Integer id) {
        participantService.deleteParticipant(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<ParticipantDto> searchParticipantsByName(@RequestParam String name) {
        return participantService.searchParticipantsByName(name);
    }

    @GetMapping("/filter")
    public List<ParticipantDto> listParticipants(
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String club,
            @RequestParam(required = false) String discipline) {
        return participantService.listParticipants(gender, age, club, discipline);
    }
}
