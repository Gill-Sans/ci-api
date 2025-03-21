package com.capit.capitinteractions.domain.checkin.aggregates;

import com.capit.capitinteractions.domain.checkin.commands.CreateCheckinCommand;
import com.capit.capitinteractions.domain.checkin.events.CheckinCreatedEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.UUID;

@Aggregate
@NoArgsConstructor
public class CheckinAggregate {
    @AggregateIdentifier
    private UUID id;
    private UUID userId;
    private UUID sessionId;

    @CommandHandler
    public CheckinAggregate(CreateCheckinCommand command) {
        AggregateLifecycle.apply(new CheckinCreatedEvent(
            command.getId(),
            command.getUserId(),
            command.getSessionId()
        ));
    }

    @EventSourcingHandler
    public void on(CheckinCreatedEvent event) {
        this.id = event.getId();
        this.userId = event.getUserId();
        this.sessionId = event.getSessionId();
    }
}
