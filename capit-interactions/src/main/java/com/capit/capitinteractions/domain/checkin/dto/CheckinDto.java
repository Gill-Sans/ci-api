package com.capit.capitinteractions.domain.checkin.dto;

import com.capit.capitinteractions.domain.checkin.entity.Checkin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data transfer object for Checkin information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckinDto {
    private UUID id;
    private UUID userId;
    private UUID conferenceId;
    private UUID sessionId;
    private LocalDateTime checkinTime;
    
    /**
     * Converts a Checkin entity to a DTO
     */
    public static CheckinDto fromEntity(Checkin checkin) {
        return CheckinDto.builder()
                .id(checkin.getId())
                .userId(checkin.getUserId())
                .conferenceId(checkin.getConferenceId())
                .sessionId(checkin.getSessionId())
                .checkinTime(checkin.getCheckinTime())
                .build();
    }
}
