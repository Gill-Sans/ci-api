package com.capit.capitschedule.domain.impl.conference.controllers;

import com.capit.capitschedule.domain.impl.conference.dto.ImportSessionsRequest;
import com.capit.capitschedule.domain.impl.conference.dto.SessionPreviewDto;
import com.capit.capitschedule.domain.impl.conference.services.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Controller for session-related operations within conferences.
 */
@RestController
@RequestMapping("/api/schedule/conferences")
@RequiredArgsConstructor
public class ConferenceSessionController {
    private final SessionService sessionService;

    /**
     * Prepare session import by fetching and transforming external data.
     */
    @PostMapping("/{conferenceId}/prepare-sessions")
    public CompletableFuture<ResponseEntity<List<SessionPreviewDto>>> prepareSessionImport(
            @PathVariable("conferenceId") UUID conferenceId,
            @RequestBody ImportSessionsRequest request) {
        return sessionService.prepareSessionImport(request, conferenceId)
                .thenApply(ResponseEntity::ok);
    }
    
    /**
     * Create sessions in the conference aggregate.
     */
    @PostMapping("/{conferenceId}/create-sessions")
    public CompletableFuture<ResponseEntity<Void>> createSessions(
            @PathVariable("conferenceId") UUID conferenceId,
            @RequestBody List<SessionPreviewDto> sessions) {
        return sessionService.createSessions(conferenceId, sessions)
                .thenApply(result -> ResponseEntity.ok().build());
    }
}
