package com.capit.capitinteractions.domain.checkin.projections;

import com.capit.capitinteractions.domain.checkin.events.CheckinCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckinDetailsProjection {

    private final CheckinDetailsRepository repository;

    @EventHandler
    public void on(CheckinCreatedEvent event) {
        CheckinDetails checkinDetails = CheckinDetails.builder()
                .id(event.getId())
                .userId(event.getUserId())
                .sessionId(event.getSessionId())
                .build();

        repository.save(checkinDetails);
    }
}
