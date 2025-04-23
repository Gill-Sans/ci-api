package com.capit.capitinteractions.domain.checkin.service;

import com.capit.capitinteractions.domain.checkin.requests.CreateCheckinRequest;
import com.capit.capitinteractions.domain.checkin.entity.Checkin;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CheckinService {
    Checkin createCheckin(CreateCheckinRequest request);
    Optional<Checkin> getCheckinById(UUID id);
    List<Checkin> getCheckinsByUserId(UUID userId);
    List<Checkin> getCheckinsBySessionId(UUID sessionId);
    boolean checkinExists(UUID userId, UUID sessionId);
} 