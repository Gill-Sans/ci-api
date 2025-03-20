package com.capit.capitschedule.domain.impl.session.entities;

import com.capit.capitschedule.domain.impl.conference.entities.Conference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Session {
    @Id
    private UUID id;

    @Column(columnDefinition = "VARCHAR(500)")
    private String name;

    @Column(columnDefinition = "VARCHAR(50)")
    private String Speaker;

    @Column(columnDefinition = "TEXT")
    private String Description;

    @Column(columnDefinition = "VARCHAR(100)")
    private String location;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "conference_id")
    private Conference conference;
}
