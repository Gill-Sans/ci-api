package com.capit.capitschedule.integration.session;


import com.capit.capitschedule.domain.impl.conference.dto.SessionDto;
import java.util.List;

public interface SessionImportStrategy {
    /**
     * Converts external API data into a list of SessionDto objects.
     * @param conferenceId The conference identifier.
     * @param externalApiPayload The raw external API data.
     * @return A list of SessionDto objects.
     */
    List<SessionDto> importSessions(String conferenceId, String externalApiPayload);
}
