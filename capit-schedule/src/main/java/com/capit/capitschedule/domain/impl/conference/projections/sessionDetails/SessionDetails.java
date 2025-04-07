package com.capit.capitschedule.domain.impl.conference.projections.sessionDetails;

import com.capit.capitschedule.domain.impl.conference.aggregates.Address;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity for storing session details in the database.
 */
@Entity
@Table(name = "sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionDetails {
    
    @Id
    private UUID sessionId;
    
    private UUID conferenceId;
    
    private String title;
    
    @Column(length = 1000)
    private String description;
    
    private LocalDateTime startTime;
    
    private LocalDateTime endTime;
    
    private String speaker;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "country", column = @Column(name = "address_country")),
        @AttributeOverride(name = "street", column = @Column(name = "address_street")),
        @AttributeOverride(name = "number", column = @Column(name = "address_number")),
        @AttributeOverride(name = "zip", column = @Column(name = "address_zip")),
        @AttributeOverride(name = "state", column = @Column(name = "address_state")),
        @AttributeOverride(name = "locationDetails", column = @Column(name = "address_location_details"))
    })
    private Address address;
    
    private String locationDetails;
    
    @Column(name = "checkin_count", nullable = false, columnDefinition = "integer default 0")
    private Integer checkinCount = 0;
    
    public SessionDetails(UUID sessionId, UUID conferenceId, String title, String description, 
                          LocalDateTime startTime, LocalDateTime endTime, String speaker) {
        this.sessionId = sessionId;
        this.conferenceId = conferenceId;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.speaker = speaker;
        this.checkinCount = 0;
    }
} 