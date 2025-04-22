package com.capit.capitschedule.domain.impl.conference.validators;

import com.capit.capitschedule.domain.impl.conference.aggregates.Address;
import com.capit.capitschedule.domain.impl.conference.aggregates.ConferenceAggregate;
import com.capit.capitschedule.domain.impl.conference.dto.CreateConferenceDto;
import com.capit.exceptions.BaseRuntimeException;
import com.capit.exceptions.ValidationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import java.time.LocalDateTime;

@Component
public class ConferenceValidatorImpl implements ConferenceValidator {
    @Override
    public void validateCreateConference(CreateConferenceDto request) {
        validateName(request.name());
        validateTimeRange(request.startTime(), request.endTime());
        validateAddress(request.location());
    }

    private void validateName(String name) throws BaseRuntimeException {
        if (!StringUtils.hasText(name)) {
            throw new ValidationException(ConferenceAggregate.class, "name", "is required");
        }

        if (name.trim().length() > 100) {
            throw new ValidationException(ConferenceAggregate.class, "name", "cannot exceed 100 characters");
        }
    }

    private void validateTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null) {
            throw new ValidationException(ConferenceAggregate.class, "start time", "cannot be null");
        }
        if (endTime == null) {
            throw new ValidationException(ConferenceAggregate.class, "end time", "cannot be null");
        }
        if (startTime.isAfter(endTime)) {
            throw new ValidationException(ConferenceAggregate.class, "start time", "cannot be after end time");
        }
        if (startTime.isEqual(endTime)) {
            throw new ValidationException(ConferenceAggregate.class, "time range", "start time and end time cannot be equal");
        }
    }

    private void validateAddress(Address address) {
        if (address == null) {
            throw new ValidationException(ConferenceAggregate.class, "address", "cannot be null");
        }

        if (!StringUtils.hasText(address.getCountry())) {
            throw new ValidationException(ConferenceAggregate.class, "address", "country cannot be empty");
        }

        if (!StringUtils.hasText(address.getStreet())) {
            throw new ValidationException(ConferenceAggregate.class, "address", "street cannot be empty");
        }

        if (!StringUtils.hasText(address.getNumber())) {
            throw new ValidationException(ConferenceAggregate.class, "address", "zip cannot be empty");
        }

        if (!StringUtils.hasText(address.getZip())) {
            throw new ValidationException(ConferenceAggregate.class, "address", "number cannot be empty");
        }

        if (!StringUtils.hasText(address.getState())) {
            throw new ValidationException(ConferenceAggregate.class, "address", "state cannot be empty");
        }
    }
}
