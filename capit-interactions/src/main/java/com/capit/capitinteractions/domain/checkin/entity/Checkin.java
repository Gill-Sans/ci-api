package com.capit.capitinteractions.domain.checkin.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "checkins")
public class Checkin {
    @Id
    private UUID id;
    private UUID userId;
    private UUID sessionId;
    private LocalDateTime checkinTime;
} 