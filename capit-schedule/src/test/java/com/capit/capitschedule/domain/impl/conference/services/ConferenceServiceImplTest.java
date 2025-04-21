package com.capit.capitschedule.domain.impl.conference.services;

import com.capit.capitschedule.domain.impl.conference.aggregates.Address;
import com.capit.capitschedule.domain.impl.conference.commands.CreateConferenceCommand;
import com.capit.capitschedule.domain.impl.conference.dto.CreateConferenceDto;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ConferenceServiceImplTest {

    @Mock
    private CommandGateway commandGateway;

    @InjectMocks
    private ConferenceServiceImpl conferenceService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateConference() {
        Address location = Address.builder()
                .country("Country")
                .street("Street")
                .number("123")
                .zip("00000")
                .state("State")
                .locationDetails("Details")
                .build();
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = LocalDateTime.now().plusDays(2);
        CreateConferenceDto dto = new CreateConferenceDto("Name", "Description", location, startTime, endTime);

        UUID result = conferenceService.createConference(dto);

        assertNotNull(result);
        ArgumentCaptor<CreateConferenceCommand> captor = ArgumentCaptor.forClass(CreateConferenceCommand.class);
        verify(commandGateway, times(1)).send(captor.capture());
        CreateConferenceCommand sentCommand = captor.getValue();

        assertEquals(result, sentCommand.getConferenceId());
        assertEquals(dto.name(), sentCommand.getName());
        assertEquals(dto.description(), sentCommand.getDescription());
        assertEquals(dto.location(), sentCommand.getLocation());
        assertEquals(dto.startTime(), sentCommand.getStartTime());
        assertEquals(dto.endTime(), sentCommand.getEndTime());
        assertEquals(0, sentCommand.getCheckinCount());
    }
}