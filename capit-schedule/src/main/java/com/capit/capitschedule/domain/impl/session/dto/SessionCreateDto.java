package com.capit.capitschedule.domain.impl.session.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record SessionCreateDto(
        String name,
        String description,
        String speaker,
        LocalDateTime startTime,
        LocalDateTime endTime,
        UUID conferenceId
) {
}
