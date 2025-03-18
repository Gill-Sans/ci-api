package com.capit.capitinteractions.domain.checkin.projections;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CheckinDetails {
    @Id
    private UUID id;
    private UUID userId;
    private UUID sessionId;
}
