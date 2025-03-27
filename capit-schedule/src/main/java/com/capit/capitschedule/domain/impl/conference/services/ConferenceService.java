package com.capit.capitschedule.domain.impl.conference.services;

import com.capit.capitschedule.domain.impl.conference.dto.CreateConferenceDto;

import java.util.UUID;

public interface ConferenceService {
    UUID createConference(CreateConferenceDto request);
}
