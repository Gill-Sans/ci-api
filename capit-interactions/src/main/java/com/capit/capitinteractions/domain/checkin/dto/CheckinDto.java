package com.capit.capitinteractions.domain.checkin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckinDto {
    private UUID id;
    private UUID userId;
    private String firstName;
    private String lastName;
    private UUID conferenceId;
    private UUID sessionId;
    private LocalDateTime checkinTime;
}
