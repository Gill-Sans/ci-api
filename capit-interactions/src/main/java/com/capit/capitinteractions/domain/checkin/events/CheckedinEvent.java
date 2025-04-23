package com.capit.capitinteractions.domain.checkin.events;

import lombok.Getter;

/**
 * Event published when a user checks in to a session
 */
@Getter
public class CheckedinEvent {
    private final String sessionId;
    private final int count;
    
    public CheckedinEvent(String sessionId, int count) {
        this.sessionId = sessionId;
        this.count = count;
    }
} 