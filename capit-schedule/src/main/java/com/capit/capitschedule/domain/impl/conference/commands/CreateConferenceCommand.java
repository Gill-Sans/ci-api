package com.capit.capitschedule.domain.impl.conference.commands;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import com.capit.capitschedule.domain.impl.conference.aggregates.Address;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@Setter
public class CreateConferenceCommand {
    @TargetAggregateIdentifier
    private final UUID conferenceId;
    private final String name;
    private final String description;
    private final String speaker;
    private final Address location;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Integer checkinCount;
}
