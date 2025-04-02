package com.capit.capitschedule.integration.session.provider;

import java.util.List;
import java.util.Map;

public interface SessionDataProvider {
    /**
     * Fetches raw session data from the external source.
     * @param conferenceId The identifier of the conference.
     * @param externalPayload The raw data from the external source.
     * @return A list of maps representing session properties.
     */
    List<Map<String, Object>> fetchSessionData(SessionDataRequest request);
}
