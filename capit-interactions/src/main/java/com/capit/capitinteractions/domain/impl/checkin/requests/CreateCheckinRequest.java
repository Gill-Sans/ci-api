package com.capit.capitinteractions.domain.impl.checkin.requests;

import java.util.UUID;

public record CreateCheckinRequest(UUID userId, UUID sessionId) {
}
