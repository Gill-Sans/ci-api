package com.capit.capitschedule.domain.impl.conference.dto;

import com.capit.capitschedule.domain.impl.conference.aggregates.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * DTO representing a session within a conference.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SessionDto {
    private UUID sessionId;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String speaker;
    private Address address;
    private String locationDetails;

    public SessionDto(UUID sessionId, String title, String description, LocalDateTime startTime,
                      LocalDateTime endTime, String speaker) {
        this.sessionId = sessionId;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.speaker = speaker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionDto that = (SessionDto) o;
        return Objects.equals(sessionId, that.sessionId) &&
               Objects.equals(title, that.title) &&
               Objects.equals(description, that.description) &&
               Objects.equals(startTime, that.startTime) &&
               Objects.equals(endTime, that.endTime) &&
               Objects.equals(speaker, that.speaker) &&
               Objects.equals(address, that.address) &&
               Objects.equals(locationDetails, that.locationDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, title, description, startTime, endTime, speaker, address, locationDetails);
    }
}
