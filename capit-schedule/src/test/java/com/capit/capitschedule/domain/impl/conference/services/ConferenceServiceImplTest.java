package com.capit.capitschedule.domain.impl.conference.services;

import com.capit.capitschedule.domain.impl.conference.aggregates.Address;
import com.capit.capitschedule.domain.impl.conference.commands.CreateConferenceCommand;
import com.capit.capitschedule.domain.impl.conference.dto.CreateConferenceDto;
import com.capit.capitschedule.domain.impl.conference.validators.ConferenceValidator;
import com.capit.exceptions.ValidationException;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ConferenceServiceImplTest {

    @Mock
    private CommandGateway commandGateway;

    @Mock
    private ConferenceValidator conferenceValidator;

    @InjectMocks
    private ConferenceServiceImpl conferenceService;

    private CreateConferenceDto validDto;
    private Address validLocation;
    private LocalDateTime validStartTime;
    private LocalDateTime validEndTime;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        validLocation = Address.builder()
                .country("Country")
                .street("Street")
                .number("123")
                .zip("00000")
                .state("State")
                .locationDetails("Details")
                .build();
        validStartTime = LocalDateTime.now().plusDays(1);
        validEndTime = LocalDateTime.now().plusDays(2);
        validDto = new CreateConferenceDto("Name", "Description", validLocation, validStartTime, validEndTime);
    }

    @Test
    public void testCreateConference_Success() {
        // Arrange
        doNothing().when(conferenceValidator).validateCreateConference(any(CreateConferenceDto.class));
        when(commandGateway.send(any(CreateConferenceCommand.class))).thenReturn(CompletableFuture.completedFuture(null));

        // Act
        UUID result = conferenceService.createConference(validDto);

        // Assert
        assertNotNull(result);
        ArgumentCaptor<CreateConferenceCommand> captor = ArgumentCaptor.forClass(CreateConferenceCommand.class);
        verify(commandGateway, times(1)).send(captor.capture());
        CreateConferenceCommand sentCommand = captor.getValue();

        assertEquals(result, sentCommand.getConferenceId());
        assertEquals(validDto.name(), sentCommand.getName());
        assertEquals(validDto.description(), sentCommand.getDescription());
        assertEquals(validDto.location(), sentCommand.getLocation());
        assertEquals(validDto.startTime(), sentCommand.getStartTime());
        assertEquals(validDto.endTime(), sentCommand.getEndTime());
        assertEquals(0, sentCommand.getCheckinCount());

        // Verify validator was called
        verify(conferenceValidator, times(1)).validateCreateConference(validDto);
    }

    @Test
    public void testCreateConference_ValidationFails() {
        // Arrange
        doThrow(new ValidationException(ConferenceValidator.class, "name", "invalid name"))
                .when(conferenceValidator).validateCreateConference(any(CreateConferenceDto.class));

        // Act & Assert
        assertThrows(ValidationException.class, () -> conferenceService.createConference(validDto));

        verify(commandGateway, never()).send(any());
    }

    @Test
    public void testCreateConference_CommandGatewayFailure() {
        // Arrange
        doNothing().when(conferenceValidator).validateCreateConference(any(CreateConferenceDto.class));
        RuntimeException commandException = new RuntimeException("Command failed");
        when(commandGateway.send(any(CreateConferenceCommand.class)))
                .thenThrow(commandException);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> conferenceService.createConference(validDto));

        assertEquals("Command failed", exception.getMessage());
        verify(conferenceValidator, times(1)).validateCreateConference(any());
    }

    @Test
    public void testCreateConference_EnsureUniqueIds() {
        // Arrange
        doNothing().when(conferenceValidator).validateCreateConference(any(CreateConferenceDto.class));

        // Act
        UUID result1 = conferenceService.createConference(validDto);
        UUID result2 = conferenceService.createConference(validDto);

        // Assert
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotEquals(result1, result2, "Conference IDs should be unique");

        ArgumentCaptor<CreateConferenceCommand> captor = ArgumentCaptor.forClass(CreateConferenceCommand.class);
        verify(commandGateway, times(2)).send(captor.capture());

        var commands = captor.getAllValues();
        assertEquals(2, commands.size());
        assertNotEquals(commands.get(0).getConferenceId(), commands.get(1).getConferenceId(),
                "Commands should have different conference IDs");
    }

    @Test
    public void testCreateConference_NullDescription() {
        // Arrange
        CreateConferenceDto dtoWithNullDescription = new CreateConferenceDto(
                "Name", null, validLocation, validStartTime, validEndTime);
        doNothing().when(conferenceValidator).validateCreateConference(any(CreateConferenceDto.class));

        // Act
        UUID result = conferenceService.createConference(dtoWithNullDescription);

        // Assert
        assertNotNull(result);
        ArgumentCaptor<CreateConferenceCommand> captor = ArgumentCaptor.forClass(CreateConferenceCommand.class);
        verify(commandGateway, times(1)).send(captor.capture());
        CreateConferenceCommand sentCommand = captor.getValue();

        assertNull(sentCommand.getDescription(), "Null description should be passed through");
    }
}