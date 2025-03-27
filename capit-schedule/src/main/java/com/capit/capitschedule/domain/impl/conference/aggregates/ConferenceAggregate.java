package com.capit.capitschedule.domain.impl.conference.aggregates;

import com.capit.capitschedule.domain.impl.conference.commands.CreateConferenceCommand;
import com.capit.capitschedule.domain.impl.conference.events.ConferenceCreatedEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.time.LocalDateTime;
import java.util.UUID;

@Aggregate
@NoArgsConstructor
public class ConferenceAggregate {
    @AggregateIdentifier
    private UUID conferenceId;
    private String name;
    private String description;
    private String speaker;
    private Address location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer checkinCount;

    @CommandHandler
    public ConferenceAggregate(CreateConferenceCommand command) {
        AggregateLifecycle.apply(new ConferenceCreatedEvent(
                command.getConferenceId(),
                command.getName(),
                command.getDescription(),
                command.getSpeaker(),
                command.getLocation(),
                command.getStartTime(),
                command.getEndTime(),
                command.getCheckinCount()
        ));
    }

    @EventSourcingHandler
    public void on(ConferenceCreatedEvent event) {
        this.conferenceId = event.getConferenceId();
        this.name = event.getName();
        this.description = event.getDescription();
        this.speaker = event.getSpeaker();
        this.location = event.getLocation();
        this.startTime = event.getStartTime();
        this.endTime = event.getEndTime();
        this.checkinCount = event.getCheckinCount();
    }
}
