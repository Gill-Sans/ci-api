package com.capit.capitschedule.domain.impl.conference.validators;

import com.capit.capitschedule.domain.impl.conference.dto.CreateConferenceDto;
import com.capit.capitschedule.domain.impl.conference.aggregates.Address;
import com.capit.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConferenceValidatorImplTest {
    private ConferenceValidatorImpl conferenceValidator;

    @BeforeEach
    public void setUp() {
        conferenceValidator = new ConferenceValidatorImpl();
    }

    @Test
    public void testValidateCreateConference_Valid() {
        CreateConferenceDto request = new CreateConferenceDto(
                "Valid Conference",
                "This is a valid description for the conference.",
                new Address("Country", "Valid Street", "180", "1500", "State", "Location Details"),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        assertDoesNotThrow(() -> conferenceValidator.validateCreateConference(request));
    }

    @Test
    public void testValidateCreateConference_NameExactlyMaxLength() {
        CreateConferenceDto request = new CreateConferenceDto(
                "A".repeat(100),
                "This is a valid description for the conference.",
                new Address("Country", "Valid Street", "180", "1500", "State", "Location Details"),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        assertDoesNotThrow(() -> conferenceValidator.validateCreateConference(request));
    }

    @Test
    public void testValidateCreateConference_NameIsNull() {
        CreateConferenceDto request = new CreateConferenceDto(
                null,
                "This is a valid description for the conference.",
                new Address("Country", "Valid Street", "180", "1500", "State", "Location Details"),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        assertThrows(ValidationException.class, () -> conferenceValidator.validateCreateConference(request));
    }

    @Test
    public void testValidateCreateConference_NameIsEmpty() {
        CreateConferenceDto request = new CreateConferenceDto(
                "",
                "This is a valid description for the conference.",
                new Address("Country", "Valid Street", "180", "1500", "State", "Location Details"),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        assertThrows(ValidationException.class, () -> conferenceValidator.validateCreateConference(request));
    }

    @Test
    public void testValidateCreateConference_NameOnlySpaces() {
        CreateConferenceDto request = new CreateConferenceDto(
                "   ",
                "This is a valid description for the conference.",
                new Address("Country", "Valid Street", "180", "1500", "State", "Location Details"),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        assertThrows(ValidationException.class, () -> conferenceValidator.validateCreateConference(request));
    }


    @Test
    public void testValidateCreateConference_NameExceedsMaxLength() {
        CreateConferenceDto request = new CreateConferenceDto(
                "A".repeat(101),
                "This is a valid description for the conference.",
                new Address("Country", "Valid Street", "180", "1500", "State", "Location Details"),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        assertThrows(ValidationException.class, () -> conferenceValidator.validateCreateConference(request));
    }

    @Test
    public void testValidateCreateConference_StartTimeIsNull() {
        CreateConferenceDto request = new CreateConferenceDto(
                "Valid Conference",
                "This is a valid description for the conference.",
                new Address("Country", "Valid Street", "180", "1500", "State", "Location Details"),
                null,
                LocalDateTime.now().plusDays(2)
        );

        assertThrows(ValidationException.class, () -> conferenceValidator.validateCreateConference(request));
    }

    @Test
    public void testValidateCreateConference_EndTimeIsNull() {
        CreateConferenceDto request = new CreateConferenceDto(
                "Valid Conference",
                "This is a valid description for the conference.",
                new Address("Country", "Valid Street", "180", "1500", "State", "Location Details"),
                LocalDateTime.now().plusDays(1),
                null
        );

        assertThrows(ValidationException.class, () -> conferenceValidator.validateCreateConference(request));
    }

    @Test
    public void testValidateCreateConference_StartTimeAfterEndTime() {
        CreateConferenceDto request = new CreateConferenceDto(
                "Valid Conference",
                "This is a valid description for the conference.",
                new Address("Country", "Valid Street", "180", "1500", "State", "Location Details"),
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(1)
        );

        assertThrows(ValidationException.class, () -> conferenceValidator.validateCreateConference(request));
    }

    @Test
    public void testValidateCreateConference_StartTimeEqualsEndTime() {
        LocalDateTime sameTime = LocalDateTime.now().plusDays(1);
        CreateConferenceDto request = new CreateConferenceDto(
                "Valid Conference",
                "This is a valid description for the conference.",
                new Address("Country", "Valid Street", "180", "1500", "State", "Location Details"),
                sameTime,
                sameTime
        );

        assertThrows(ValidationException.class, () -> conferenceValidator.validateCreateConference(request));
    }

    @Test
    public void testValidateCreateConference_AddressIsNull() {
        CreateConferenceDto request = new CreateConferenceDto(
                "Valid Conference",
                "This is a valid description for the conference.",
                null,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        assertThrows(ValidationException.class, () -> conferenceValidator.validateCreateConference(request));
    }

    @Test
    public void testValidateCreateConference_AddressCountryIsEmpty() {
        CreateConferenceDto request = new CreateConferenceDto(
                "Valid Conference",
                "This is a valid description for the conference.",
                new Address("", "Valid Street", "180", "1500", "State", "Location Details"),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        assertThrows(ValidationException.class, () -> conferenceValidator.validateCreateConference(request));
    }

    @Test
    public void testValidateCreateConference_AddressCountryOnlySpaces() {
        CreateConferenceDto request = new CreateConferenceDto(
                "Valid Conference",
                "This is a valid description for the conference.",
                new Address("   ", "Valid Street", "180", "1500", "State", "Location Details"),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        assertThrows(ValidationException.class, () -> conferenceValidator.validateCreateConference(request));
    }

    @Test
    public void testValidateCreateConference_AddressStreetIsEmpty() {
        CreateConferenceDto request = new CreateConferenceDto(
                "Valid Conference",
                "This is a valid description for the conference.",
                new Address("Country", "", "180", "1500", "State", "Location Details"),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        assertThrows(ValidationException.class, () -> conferenceValidator.validateCreateConference(request));
    }

    @Test
    public void testValidateCreateConference_AddressStreetOnlySpaces() {
        CreateConferenceDto request = new CreateConferenceDto(
                "Valid Conference",
                "This is a valid description for the conference.",
                new Address("Country", "   ", "180", "1500", "State", "Location Details"),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        assertThrows(ValidationException.class, () -> conferenceValidator.validateCreateConference(request));
    }

    @Test
    public void testValidateCreateConference_AddressNumberIsEmpty() {
        CreateConferenceDto request = new CreateConferenceDto(
                "Valid Conference",
                "This is a valid description for the conference.",
                new Address("Country", "Valid Street", "", "1500", "State", "Location Details"),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        assertThrows(ValidationException.class, () -> conferenceValidator.validateCreateConference(request));
    }

    @Test
    public void testValidateCreateConference_AddressNumberOnlySpaces() {
        CreateConferenceDto request = new CreateConferenceDto(
                "Valid Conference",
                "This is a valid description for the conference.",
                new Address("Country", "Valid Street", "   ", "1500", "State", "Location Details"),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        assertThrows(ValidationException.class, () -> conferenceValidator.validateCreateConference(request));
    }

    @Test
    public void testValidateCreateConference_AddressZipIsEmpty() {
        CreateConferenceDto request = new CreateConferenceDto(
                "Valid Conference",
                "This is a valid description for the conference.",
                new Address("Country", "Valid Street", "180", "", "State", "Location Details"),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        assertThrows(ValidationException.class, () -> conferenceValidator.validateCreateConference(request));
    }

    @Test
    public void testValidateCreateConference_AddressZipOnlySpaces() {
        CreateConferenceDto request = new CreateConferenceDto(
                "Valid Conference",
                "This is a valid description for the conference.",
                new Address("Country", "Valid Street", "180", "   ", "State", "Location Details"),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        assertThrows(ValidationException.class, () -> conferenceValidator.validateCreateConference(request));
    }

    @Test
    public void testValidateCreateConference_AddressStateIsEmpty() {
        CreateConferenceDto request = new CreateConferenceDto(
                "Valid Conference",
                "This is a valid description for the conference.",
                new Address("Country", "Valid Street", "180", "1500", "", "Location Details"),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        assertThrows(ValidationException.class, () -> conferenceValidator.validateCreateConference(request));
    }

    @Test
    public void testValidateCreateConference_AddressStateOnlySpaces() {
        CreateConferenceDto request = new CreateConferenceDto(
                "Valid Conference",
                "This is a valid description for the conference.",
                new Address("Country", "Valid Street", "180", "1500", "   ", "Location Details"),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        assertThrows(ValidationException.class, () -> conferenceValidator.validateCreateConference(request));
    }
}
