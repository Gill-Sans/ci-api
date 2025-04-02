package com.capit.capitschedule.integration.session.strategy;

import com.capit.capitschedule.domain.impl.conference.dto.SessionDto;
import java.util.List;

public interface SessionImportStrategy {
    /**
     * Converts external API data into a list of SessionDto objects.
     */
    List<SessionDto> importSessions(String conferenceId, String externalApiPayload);
}
