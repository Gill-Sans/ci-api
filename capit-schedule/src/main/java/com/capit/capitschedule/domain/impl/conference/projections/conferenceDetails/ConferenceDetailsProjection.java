package com.capit.capitschedule.domain.impl.conference.projections.conferenceDetails;

import com.capit.capitschedule.domain.impl.conference.events.ConferenceCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConferenceDetailsProjection {
    private final ConferenceDetailsRepository conferenceDetailsRepository;

    @EventHandler
    public void on(ConferenceCreatedEvent event) {
        ConferenceDetails conferenceDetails = ConferenceDetails.builder()
                .id(event.getConferenceId())
                .title(event.getTitle())
                .description(event.getDescription())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .build();

        conferenceDetailsRepository.save(conferenceDetails);
    }
}
