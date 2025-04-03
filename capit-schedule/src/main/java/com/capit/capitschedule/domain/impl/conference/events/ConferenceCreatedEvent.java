package com.capit.capitschedule.domain.impl.conference.events;

import com.capit.capitschedule.domain.impl.conference.aggregates.Address;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ConferenceCreatedEvent {
    private final UUID conferenceId;
    private final String name;
    private final String description;
    private final Address location;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Integer checkinCount;
}
