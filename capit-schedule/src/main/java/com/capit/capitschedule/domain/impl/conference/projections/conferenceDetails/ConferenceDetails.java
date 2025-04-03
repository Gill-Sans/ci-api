package com.capit.capitschedule.domain.impl.conference.projections.conferenceDetails;

import com.capit.capitschedule.domain.impl.conference.aggregates.Address;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConferenceDetails {
    @Id
    private UUID id;
    private String name;
    private String description;
    @Embedded
    private Address location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer checkinCount;
}
