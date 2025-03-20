package com.capit.capitschedule.domain.impl.session.services;

import com.capit.capitschedule.domain.impl.session.dto.SessionCreateDto;
import com.capit.capitschedule.domain.impl.session.dto.SessionDetailDto;
import com.capit.capitschedule.domain.impl.session.dto.SessionSummaryDto;
import com.capit.capitschedule.domain.impl.session.entities.Session;
import com.capit.capitschedule.domain.impl.session.repositories.SessionRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;
    private final ObjectMapper objectMapper;

    @Override
    public List<SessionSummaryDto> findByConference(UUID conferenceId) {
        List<Session> sessions = sessionRepository.findAllByConferenceId(conferenceId);
        return objectMapper.convertValue(sessions, new TypeReference<>(){});
    }

    @Override
    public SessionDetailDto createSession(SessionCreateDto dto) {
        Session session = objectMapper.convertValue(dto, Session.class);
        sessionRepository.save(session);
        return objectMapper.convertValue(session, SessionDetailDto.class);
    }
}
