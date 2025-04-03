package com.capit.capitschedule.domain.impl.conference.commands;

import com.capit.capitschedule.domain.impl.conference.dto.SessionDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ImportSessionsCommand {
    @TargetAggregateIdentifier
    private final UUID conferenceId;
    private final List<SessionDto> sessions;
}
