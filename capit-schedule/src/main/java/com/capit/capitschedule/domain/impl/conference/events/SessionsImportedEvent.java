package com.capit.capitschedule.domain.impl.conference.events;

import com.capit.capitschedule.domain.impl.conference.dto.SessionDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class SessionsImportedEvent {
    private final UUID conferenceId;
    private final List<SessionDto> sessions;
}
