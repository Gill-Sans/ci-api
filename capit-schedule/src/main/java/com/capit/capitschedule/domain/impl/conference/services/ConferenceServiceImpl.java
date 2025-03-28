package com.capit.capitschedule.domain.impl.conference.services;

import com.capit.capitschedule.domain.impl.conference.commands.CreateConferenceCommand;
import com.capit.capitschedule.domain.impl.conference.dto.CreateConferenceDto;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConferenceServiceImpl implements ConferenceService {
    private final CommandGateway commandGateway;

    @Override
    public UUID createConference(CreateConferenceDto request) {
        UUID conferenceId = UUID.randomUUID();
        commandGateway.send(new CreateConferenceCommand(
                conferenceId,
                request.name(),
                request.description(),
                request.speaker(),
                request.location(),
                request.startTime(),
                request.endTime(),
                0
        ));
        return conferenceId;
    }
}
