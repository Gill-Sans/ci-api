package com.capit.capitinteractions.domain.checkin.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CheckinEventMessage implements WsEvent{
    private final EventType type;
    private final CheckinDto checkin;

    @Override
    public EventType getType() { return type; }
}
