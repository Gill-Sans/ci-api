package com.capit.capitschedule.domain.impl.conference.dto;

import com.capit.capitschedule.domain.impl.conference.aggregates.Address;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * DTO representing a session preview during the import preparation phase.
 * This doesn't include an ID since the session doesn't exist in the system yet.
 */
public class SessionPreviewDto {
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String speaker;
    private Address address;
    private String locationDetails;

    // Default constructor for serialization
    public SessionPreviewDto() {
    }

    public SessionPreviewDto(String title, String description, LocalDateTime startTime,
                             LocalDateTime endTime, String speaker) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.speaker = speaker;
    }

    public SessionPreviewDto(String title, String description, LocalDateTime startTime,
                            LocalDateTime endTime, String speaker, Address address, String locationDetails) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.speaker = speaker;
        this.address = address;
        this.locationDetails = locationDetails;
    }

    /**
     * Convert this preview DTO to a full SessionDto with a null ID.
     */
    public SessionDto toSessionDto() {
        return new SessionDto(
            null,
            title,
            description,
            startTime,
            endTime,
            speaker,
            address,
            locationDetails
        );
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getLocationDetails() {
        return locationDetails;
    }

    public void setLocationDetails(String locationDetails) {
        this.locationDetails = locationDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionPreviewDto that = (SessionPreviewDto) o;
        return Objects.equals(title, that.title) &&
               Objects.equals(description, that.description) &&
               Objects.equals(startTime, that.startTime) &&
               Objects.equals(endTime, that.endTime) &&
               Objects.equals(speaker, that.speaker) &&
               Objects.equals(address, that.address) &&
               Objects.equals(locationDetails, that.locationDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, startTime, endTime, speaker, address, locationDetails);
    }
} 