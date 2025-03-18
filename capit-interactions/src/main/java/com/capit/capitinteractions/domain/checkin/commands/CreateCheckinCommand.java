package com.capit.capitinteractions.domain.checkin.commands;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateCheckinCommand extends BaseCommand<UUID>{
    private final UUID userId;
    private final UUID sessionId;

    public CreateCheckinCommand(UUID id, UUID userId, UUID sessionId) {
        super(id);
        this.userId = userId;
        this.sessionId = sessionId;
    }
}
