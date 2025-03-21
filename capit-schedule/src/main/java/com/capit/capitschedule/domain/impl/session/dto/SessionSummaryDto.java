package com.capit.capitschedule.domain.impl.session.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionSummaryDto {
    private String sessionId;
    private String name;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private UUID conferenceId;
}
