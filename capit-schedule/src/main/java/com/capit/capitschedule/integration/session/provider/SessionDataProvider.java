package com.capit.capitschedule.integration.session.provider;

import java.util.List;
import java.util.Map;

public interface SessionDataProvider {
    /**
     * Fetches raw session data from the external source.
     * @param request The request object containing parameters for fetching session data.
     * @return A list of maps representing session properties.
     */
    List<Map<String, Object>> fetchSessionData(SessionDataRequest request);
}
