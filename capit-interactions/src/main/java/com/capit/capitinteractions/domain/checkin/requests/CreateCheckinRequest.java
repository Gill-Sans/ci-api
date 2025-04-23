package com.capit.capitinteractions.domain.checkin.requests;

import java.util.UUID;

public record CreateCheckinRequest(UUID userId, UUID sessionId) {
} 