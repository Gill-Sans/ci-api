package com.capit.capitschedule.integration.session.impl;

import com.capit.capitschedule.domain.impl.conference.dto.SessionDto;
import com.capit.capitschedule.integration.session.SessionImportMetadata;
import com.capit.capitschedule.integration.session.SessionImportStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@SessionImportMetadata(strategy = "adapter", displayName = "Techorama API Adapter")
public class TechoramaSessionImportStrategy implements SessionImportStrategy {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<SessionDto> importSessions(String conferenceId, String externalApiPayload) {
        try {
            // Parse the JSON payload into a list of maps.
            List<Map<String, Object>> sessionsData = objectMapper.readValue(
                    externalApiPayload,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class)
            );

            // Date format for parsing combined date and time.
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");

            return sessionsData.stream().map(data -> {
                // Extract fields from the JSON.
                String title = (String) data.get("title");
                String room = (String) data.get("room");
                String speakers = (String) data.get("speakers");

                // Construct a description that includes room and speaker info.
                String description = "";
                if (room != null && !room.isBlank()) {
                    description = "Room: " + room;
                }
                if (speakers != null && !speakers.isBlank()) {
                    description += (description.isEmpty() ? "" : ", ") + "Speakers: " + speakers;
                }

                // Example: "27 May 2025 | 07:30 - 08:30"
                String dateField = (String) data.get("date");
                // Split into date and time range.
                String[] parts = dateField.split("\\|");
                String datePart = parts[0].trim(); // e.g., "27 May 2025"
                String timeRange = parts[1].trim(); // e.g., "07:30 - 08:30"
                String[] timeParts = timeRange.split("-");
                String startTimeStr = datePart + " " + timeParts[0].trim(); // "27 May 2025 07:30"
                String endTimeStr = datePart + " " + timeParts[1].trim();   // "27 May 2025 08:30"

                LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
                LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);

                // Create a new SessionDto with a generated sessionId.
                return new SessionDto(
                        UUID.randomUUID(),
                        title,
                        description,
                        startTime,
                        endTime,
                        speakers
                );
            }).toList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}