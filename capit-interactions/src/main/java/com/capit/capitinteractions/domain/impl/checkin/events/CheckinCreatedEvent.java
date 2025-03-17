package com.capit.capitinteractions.domain.impl.checkin.events;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CheckinCreatedEvent extends BaseEvent<UUID> {
    private final UUID userId;
    private final UUID sessionId;

    public CheckinCreatedEvent(UUID id, UUID userId, UUID sessionId) {
        super(id);
        this.userId = userId;
        this.sessionId = sessionId;
    }
}
