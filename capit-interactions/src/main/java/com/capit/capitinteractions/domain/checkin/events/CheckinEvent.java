package com.capit.capitinteractions.domain.checkin.events;

/**
 * Event published when a user checks in to a session
 */
public class CheckinEvent {
    private final String sessionId;
    private final int count;
    
    public CheckinEvent(String sessionId, int count) {
        this.sessionId = sessionId;
        this.count = count;
    }
    
    public String getSessionId() {
        return sessionId;
    }
    
    public int getCount() {
        return count;
    }
} 