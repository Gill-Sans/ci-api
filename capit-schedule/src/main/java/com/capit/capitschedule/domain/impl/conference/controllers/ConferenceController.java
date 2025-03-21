package com.capit.capitschedule.domain.impl.conference.controllers;

import com.capit.capitschedule.domain.impl.conference.commands.CreateConferenceCommand;
import com.capit.capitschedule.domain.impl.conference.projections.conferenceDetails.ConferenceDetails;
import com.capit.capitschedule.domain.impl.conference.projections.conferenceDetails.ConferenceDetailsRepository;
import com.capit.capitschedule.domain.impl.conference.requests.CreateConferenceRequest;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/conferences")
@RequiredArgsConstructor
public class ConferenceController {
    private final CommandGateway commandGateway;
    private final ConferenceDetailsRepository conferenceDetailsRepository;

    @GetMapping
    public ResponseEntity<List<ConferenceDetails>> getConferences() {
        return ResponseEntity.ok(conferenceDetailsRepository.findAll());
    }

    @GetMapping("/{conferenceId}")
    public ResponseEntity<ConferenceDetails> getConference(@PathVariable UUID conferenceId) {
        return ResponseEntity.ok(conferenceDetailsRepository.findById(conferenceId)
                .orElseThrow(() -> new IllegalArgumentException("Conference not found"))
        );
    }

    @PostMapping
    public ResponseEntity<UUID> createConference(@RequestBody CreateConferenceRequest request) {
        UUID conferenceId = UUID.randomUUID();
        CreateConferenceCommand command = new CreateConferenceCommand(
            conferenceId,
            request.title(),
            request.description(),
            request.startTime(),
            request.endTime()
        );

        commandGateway.sendAndWait(command);
        return ResponseEntity.ok(conferenceId);
    }
}
