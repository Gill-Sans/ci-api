package com.capit.capitschedule.domain.impl.session.services;

import com.capit.capitschedule.domain.impl.session.dto.SessionCreateDto;
import com.capit.capitschedule.domain.impl.session.dto.SessionDetailDto;
import com.capit.capitschedule.domain.impl.session.dto.SessionSummaryDto;

import java.util.List;
import java.util.UUID;

public interface SessionService {
    public List<SessionSummaryDto> findByConference(UUID conferenceId);
    public SessionDetailDto createSession(SessionCreateDto dto);
}
