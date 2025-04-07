package com.capit.capitschedule.domain.impl.conference.dto;

import com.capit.capitschedule.domain.impl.conference.aggregates.Address;
import com.capit.capitschedule.domain.impl.conference.projections.sessionDetails.SessionDetails;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
public class SessionDetailsDto {
    private UUID sessionId;
    private UUID conferenceId;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String speaker;
    private Address address;
    private String locationDetails;
    private Integer checkinCount;

    public static SessionDetailsDto fromEntity(SessionDetails entity) {
        return SessionDetailsDto.builder()
                .sessionId(entity.getSessionId())
                .conferenceId(entity.getConferenceId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .speaker(entity.getSpeaker())
                .address(entity.getAddress())
                .locationDetails(entity.getLocationDetails())
                .checkinCount(entity.getCheckinCount())
                .build();
    }

    public static List<SessionDetailsDto> fromEntities(List<SessionDetails> entities) {
        return entities.stream()
                .map(SessionDetailsDto::fromEntity)
                .collect(Collectors.toList());
    }
} 