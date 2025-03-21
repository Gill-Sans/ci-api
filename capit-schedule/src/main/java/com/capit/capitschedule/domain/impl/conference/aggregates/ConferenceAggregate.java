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
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @CommandHandler
    public ConferenceAggregate(CreateConferenceCommand command) {
        AggregateLifecycle.apply(new ConferenceCreatedEvent(
            command.getConferenceId(),
            command.getTitle(),
            command.getDescription(),
            command.getStartTime(),
            command.getEndTime()
        ));
    }

    @EventSourcingHandler
    public void on(ConferenceCreatedEvent event) {
        this.conferenceId = event.getConferenceId();
        this.title = event.getTitle();
        this.description = event.getDescription();
        this.startTime = event.getStartTime();
        this.endTime = event.getEndTime();
    }
}
