package com.capit.capitinteractions.domain.checkin.requests;

import com.capit.capitinteractions.domain.checkin.dto.CheckinType;

import java.util.UUID;

public record CheckinRequest(
    CheckinType type,
    UUID sessionId,
    UUID conferenceId,
    UUID userId
) {
} 