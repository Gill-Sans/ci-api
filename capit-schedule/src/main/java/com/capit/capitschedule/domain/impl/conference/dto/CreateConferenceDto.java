package com.capit.capitschedule.domain.impl.conference.dto;

import com.capit.capitschedule.domain.impl.conference.aggregates.Address;

import java.time.LocalDateTime;

public record CreateConferenceDto(String name, String description, String speaker, Address location, LocalDateTime startTime, LocalDateTime endTime) {
}

