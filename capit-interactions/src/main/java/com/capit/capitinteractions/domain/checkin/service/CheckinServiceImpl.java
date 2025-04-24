package com.capit.capitinteractions.domain.checkin.service;

import com.capit.capitinteractions.domain.checkin.requests.CreateCheckinRequest;
import com.capit.capitinteractions.domain.checkin.entity.Checkin;
import com.capit.capitinteractions.domain.checkin.events.CheckedinEvent;
import com.capit.capitinteractions.domain.checkin.repository.CheckinRepository;
import com.capit.exceptions.BaseRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckinServiceImpl implements CheckinService {

    private final CheckinRepository checkinRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public Checkin createCheckin(CreateCheckinRequest request) {
        if (checkinRepository.existsByUserIdAndSessionId(request.userId(), request.sessionId())) {
            throw new BaseRuntimeException("Checkin already exists for userId: " + request.userId() + " and sessionId: " + request.sessionId(), HttpStatus.CONFLICT);
        }

        Checkin checkin = new Checkin();
        checkin.setId(UUID.randomUUID());
        checkin.setUserId(request.userId());
        checkin.setSessionId(request.sessionId());
        checkin.setCheckinTime(LocalDateTime.now());

        Checkin savedCheckin = checkinRepository.save(checkin);

        int newCount = checkinRepository.findBySessionId(request.sessionId()).size();

        String sessionId = request.sessionId().toString();
        eventPublisher.publishEvent(new CheckedinEvent(sessionId, newCount));
        
        return savedCheckin;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Checkin> getCheckinById(UUID id) {
        return checkinRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Checkin> getCheckinsByUserId(UUID userId) {
        return checkinRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Checkin> getCheckinsBySessionId(UUID sessionId) {
        return checkinRepository.findBySessionId(sessionId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkinExists(UUID userId, UUID sessionId) {
        return checkinRepository.existsByUserIdAndSessionId(userId, sessionId);
    }
} 