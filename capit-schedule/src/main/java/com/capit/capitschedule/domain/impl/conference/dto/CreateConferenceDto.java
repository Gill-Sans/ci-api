package com.capit.capitschedule.domain.impl.conference.dto;

import java.time.LocalDateTime;

public record CreateConferenceDto(String title, String description, LocalDateTime startTime, LocalDateTime endTime) {
}
