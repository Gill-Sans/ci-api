package com.capit.capitinteractions.infrastructure.messaging.kafka;

import com.capit.capitinteractions.api.checkin.controllers.CheckinSseController;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Component
public class CheckinConsumer {
    private final Logger logger = LoggerFactory.getLogger(CheckinConsumer.class);
    private final Map<String, Integer> sessionCount = new ConcurrentHashMap<>();
    private final CheckinSseController sseController;

    @KafkaListener(topics = "checkin-details", groupId = "checkin-group")
    public void consume(String sessionId) {
        sessionCount.compute(sessionId, (k,v) -> (v == null) ? 1 : v+1);

        int newCount = sessionCount.get(sessionId);
        logger.info("Checkin details received for session {} => new count = {}", sessionId, newCount);

        sseController.sendCheckinUpdate(sessionId, newCount);
    }
}
