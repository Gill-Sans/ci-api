package com.capit.capitschedule.domain.impl.conference.requests;

import java.time.LocalDateTime;

public record CreateConferenceRequest(String title, String description, LocalDateTime startTime, LocalDateTime endTime) {
}
