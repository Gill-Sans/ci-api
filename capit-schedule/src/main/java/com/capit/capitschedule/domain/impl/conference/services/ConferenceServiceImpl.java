package com.capit.capitschedule.domain.impl.conference.services;

import com.capit.capitschedule.domain.impl.conference.commands.CreateConferenceCommand;
import com.capit.capitschedule.domain.impl.conference.dto.CreateConferenceDto;
import com.capit.capitschedule.domain.impl.conference.validators.ConferenceValidator;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConferenceServiceImpl implements ConferenceService {
    private final CommandGateway commandGateway;
    private final ConferenceValidator conferenceValidator;

    @Override
    public UUID createConference(CreateConferenceDto request) {
        conferenceValidator.validateCreateConference(request);

        UUID conferenceId = UUID.randomUUID();
        commandGateway.send(new CreateConferenceCommand(
                conferenceId,
                request.name(),
                request.description(),
                request.location(),
                request.startTime(),
                request.endTime(),
                0
        ));
        return conferenceId;
    }
}
