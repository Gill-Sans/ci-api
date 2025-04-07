package com.capit.capitschedule.domain.impl.conference.controllers;

import com.capit.capitschedule.domain.impl.conference.dto.SessionDetailsDto;
import com.capit.capitschedule.domain.impl.conference.projections.sessionDetails.SessionDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Controller for querying session data.
 */
@RestController
@RequestMapping("/api/schedule/sessions")
@RequiredArgsConstructor
public class SessionController {
    
    private final SessionDetailsRepository sessionDetailsRepository;

    @GetMapping
    public ResponseEntity<List<SessionDetailsDto>> getAllSessions() {
        return ResponseEntity.ok(SessionDetailsDto.fromEntities(sessionDetailsRepository.findAll()));
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<SessionDetailsDto> getSessionById(@PathVariable UUID sessionId) {
        return sessionDetailsRepository.findById(sessionId)
                .map(SessionDetailsDto::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/conference/{conferenceId}")
    public ResponseEntity<List<SessionDetailsDto>> getSessionsByConferenceId(@PathVariable UUID conferenceId) {
        return ResponseEntity.ok(SessionDetailsDto.fromEntities(sessionDetailsRepository.findByConferenceId(conferenceId)));
    }
} 