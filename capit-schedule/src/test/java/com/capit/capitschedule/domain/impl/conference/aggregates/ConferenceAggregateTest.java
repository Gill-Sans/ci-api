package com.capit.capitschedule.domain.impl.conference.aggregates;

import com.capit.capitschedule.domain.impl.conference.commands.CreateConferenceCommand;
import com.capit.capitschedule.domain.impl.conference.events.ConferenceCreatedEvent;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

public class ConferenceAggregateTest {

    private FixtureConfiguration<ConferenceAggregate> fixture;

    @BeforeEach
    public void setUp() {
        fixture = new AggregateTestFixture<>(ConferenceAggregate.class);
    }

    @Test
    public void testConferenceCreation() {
        UUID conferenceId = UUID.randomUUID();
        String name = "Test Conference";
        String description = "A test description";
        Address location = Address.builder()
                .country("Country")
                .street("Street")
                .number("123")
                .zip("00000")
                .state("State")
                .locationDetails("Near the park")
                .build();
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = LocalDateTime.now().plusDays(2);
        int checkinCount = 0;

        CreateConferenceCommand command = new CreateConferenceCommand(
                conferenceId, name, description, location, startTime, endTime, checkinCount
        );

        ConferenceCreatedEvent expectedEvent = new ConferenceCreatedEvent(
                conferenceId, name, description, location, startTime, endTime, checkinCount
        );

        fixture.givenNoPriorActivity()
                .when(command)
                .expectEvents(expectedEvent);
    }
}
