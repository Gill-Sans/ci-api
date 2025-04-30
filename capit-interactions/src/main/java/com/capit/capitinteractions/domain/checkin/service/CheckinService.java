package com.capit.capitinteractions.domain.checkin.service;

import com.capit.capitinteractions.domain.checkin.dto.CheckinDto;
import com.capit.capitinteractions.domain.checkin.requests.CheckinRequest;
import com.capit.capitinteractions.domain.checkin.entity.Checkin;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface CheckinService {
    CheckinDto createCheckin(CheckinRequest request);
    Optional<CheckinDto> getCheckinById(UUID id);
    List<CheckinDto> getCheckinsByUserId(UUID userId);
    List<CheckinDto> getCheckinsBySessionId(UUID sessionId);
    List<CheckinDto> getCheckinsByConferenceId(UUID conferenceId);
    boolean checkinExists(UUID userId, UUID sessionId);
}