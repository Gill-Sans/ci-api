package com.capit.capitinteractions.domain.checkin.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckinKafkaEvent {
    private String sessionId;
    private String conferenceId;
    private String userId;
    private String firstName;
    private String lastName;
    private String instanceId;
}