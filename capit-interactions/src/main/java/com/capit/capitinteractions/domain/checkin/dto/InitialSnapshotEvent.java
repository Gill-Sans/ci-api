package com.capit.capitinteractions.domain.checkin.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class InitialSnapshotEvent implements WsEvent {
    private final EventType type = EventType.INITIAL_SNAPSHOT;
    private final List<CheckinDto> checkins;

    @Override
    public EventType getType() { return type; }
}
