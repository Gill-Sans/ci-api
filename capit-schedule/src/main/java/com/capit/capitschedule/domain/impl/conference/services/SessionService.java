package com.capit.capitschedule.domain.impl.conference.services;

import com.capit.capitschedule.domain.impl.conference.dto.ImportSessionsRequest;
import com.capit.capitschedule.domain.impl.conference.dto.SessionDto;
import com.capit.capitschedule.domain.impl.conference.dto.SessionPreviewDto;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Service for managing sessions within conferences.
 */
public interface SessionService {
    /**
     * Import and transform session data without persisting them.
     */
    CompletableFuture<List<SessionPreviewDto>> prepareSessionImport(ImportSessionsRequest request, UUID conferenceId);
    
    /**
     * Create sessions in the conference aggregate.
     */
    CompletableFuture<Void> createSessions(UUID conferenceId, List<SessionPreviewDto> sessions);
}
