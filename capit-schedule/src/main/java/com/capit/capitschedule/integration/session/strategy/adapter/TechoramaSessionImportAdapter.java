package com.capit.capitschedule.integration.session.strategy.adapter;

import com.capit.capitschedule.domain.impl.conference.aggregates.Address;
import com.capit.capitschedule.domain.impl.conference.dto.SessionDto;
import com.capit.capitschedule.integration.session.strategy.SessionImportMetadata;
import com.capit.capitschedule.integration.session.strategy.SessionImportStrategyType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@SessionImportMetadata(strategy = SessionImportStrategyType.ADAPTER, displayName = "Techorama API Adapter")
public class TechoramaSessionImportAdapter implements AdapterSessionImportStrategy {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<SessionDto> importSessions(String conferenceId, String externalApiPayload) {
        try {
            List<Map<String, Object>> sessionsData = objectMapper.readValue(
                    externalApiPayload,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class)
            );

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");

            return sessionsData.stream().map(data -> {
                String title = (String) data.get("title");
                String room = (String) data.get("room");
                Integer roomInt = (Integer) data.get("roomInt");
                String speakers = (String) data.get("speakers");

                String locationDetails = null;
                if (room != null && !room.isBlank()) {
                    locationDetails = room;
                    if (roomInt != null) {
                        locationDetails += " (" + roomInt + ")";
                    }
                }

                String description = "";

                String dateField = (String) data.get("date");
                String[] parts = dateField.split("\\|");
                String datePart = parts[0].trim();
                String timeRange = parts[1].trim();
                String[] timeParts = timeRange.split("-");
                String startTimeStr = datePart + " " + timeParts[0].trim();
                String endTimeStr = datePart + " " + timeParts[1].trim();

                LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
                LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);

                Address address = null;
                
                return new SessionDto(
                        UUID.randomUUID(),
                        title,
                        description,
                        startTime,
                        endTime,
                        speakers,
                        address,
                        locationDetails
                );
            }).toList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}