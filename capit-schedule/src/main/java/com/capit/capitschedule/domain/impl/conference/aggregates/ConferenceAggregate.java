package com.capit.capitschedule.domain.impl.conference.aggregates;

import com.capit.capitschedule.domain.impl.conference.commands.CreateConferenceCommand;
import com.capit.capitschedule.domain.impl.conference.commands.ImportSessionsCommand;
import com.capit.capitschedule.domain.impl.conference.dto.SessionDto;
import com.capit.capitschedule.domain.impl.conference.events.ConferenceCreatedEvent;
import com.capit.capitschedule.domain.impl.conference.events.SessionsImportedEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    private List<SessionDto> sessions = new ArrayList<>();

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

    @CommandHandler
    public void handle(ImportSessionsCommand command) {
        List<SessionDto> sessionsWithIds = command.getSessions().stream()
            .map(session -> {
                if (session.getSessionId() == null) {
                    return new SessionDto(
                        UUID.randomUUID(),
                        session.getTitle(),
                        session.getDescription(),
                        session.getStartTime(),
                        session.getEndTime(),
                        session.getSpeaker(),
                        session.getAddress(),
                        session.getLocationDetails()
                    );
                }
                return session;
            })
            .collect(Collectors.toList());
            
        AggregateLifecycle.apply(new SessionsImportedEvent(
                command.getConferenceId(),
                sessionsWithIds
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

    @EventSourcingHandler
    public void on(SessionsImportedEvent event) {
        this.sessions.addAll(event.getSessions());
    }
}
