package com.example.eksamen24backend.repository;

import com.example.eksamen24backend.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
    List<Participant> findByName(String name);
}

