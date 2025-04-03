package com.capit.capitschedule.integration.session.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class HttpSessionDataProvider implements SessionDataProvider {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<Map<String, Object>> fetchSessionData(SessionDataRequest request) {
        try {
            String url = request.getUrl();
            String jsonResponse = restTemplate.getForObject(url, String.class);
            return objectMapper.readValue(
                    jsonResponse,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class)
            );
        } catch (Exception e) {
            String message = "Error fetching session data from URL: " + request.getUrl() + " with method: " + request.getMethod();
            log.error(message, e);
            throw new RuntimeException(message, e);
        }
    }
}
