package com.capit.capitinteractions.api.checkin.requests;

import java.util.UUID;

public record CreateCheckinRequest(UUID userId, UUID sessionId) {
} 