package com.capit.capitschedule.domain.impl.conference.validators;

import com.capit.capitschedule.domain.impl.conference.dto.CreateConferenceDto;

public interface ConferenceValidator {
    void validateCreateConference(CreateConferenceDto request);
}
