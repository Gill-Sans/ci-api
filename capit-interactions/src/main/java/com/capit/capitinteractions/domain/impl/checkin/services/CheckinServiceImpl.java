package com.capit.capitinteractions.domain.impl.checkin.services;

import com.capit.capitinteractions.domain.impl.checkin.commands.CreateCheckinCommand;
import com.capit.capitinteractions.domain.impl.checkin.requests.CreateCheckinRequest;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CheckinServiceImpl implements CheckinServcie {
    private final CommandGateway commandGateway;

    @Override
    public String createCheckin(CreateCheckinRequest request) {
        UUID checkinId = UUID.randomUUID();
        CreateCheckinCommand command = new CreateCheckinCommand(
                checkinId,
                request.userId(),
                request.sessionId()
        );

        commandGateway.sendAndWait(command);
        return checkinId.toString();
    }
}
