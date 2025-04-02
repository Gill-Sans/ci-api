package com.capit.capitschedule.domain.impl.conference.projections.sessionDetails;

import com.capit.capitschedule.domain.impl.conference.events.SessionsImportedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Projection that listens for session-related events and updates the database accordingly.
 */
@Component
@RequiredArgsConstructor
public class SessionDetailsProjection {
    
    private final SessionDetailsRepository sessionDetailsRepository;
    
    @EventHandler
    public void on(SessionsImportedEvent event) {
        List<SessionDetails> sessionDetailsList = new ArrayList<>();
        
        event.getSessions().forEach(sessionDto -> {
            SessionDetails sessionDetails = new SessionDetails(
                    sessionDto.getSessionId(),
                    event.getConferenceId(),
                    sessionDto.getTitle(),
                    sessionDto.getDescription(),
                    sessionDto.getStartTime(),
                    sessionDto.getEndTime(),
                    sessionDto.getSpeaker()
            );
            
            // Set the optional fields
            sessionDetails.setAddress(sessionDto.getAddress());
            sessionDetails.setLocationDetails(sessionDto.getLocationDetails());
            
            sessionDetailsList.add(sessionDetails);
        });
        
        sessionDetailsRepository.saveAll(sessionDetailsList);
    }
} 