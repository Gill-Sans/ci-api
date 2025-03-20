package com.capit.capitinteractions.infrastructure.messaging.kafka;

import com.capit.capitinteractions.domain.checkin.events.CheckinCreatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class CheckinPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Logger logger = LoggerFactory.getLogger(CheckinPublisher.class);

    public CheckinPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @EventHandler
    public void on(CheckinCreatedEvent event) {
        String payload = "{" +
            "\"checkinId\":\"" + event.getId() + "\"," +
            "\"sessionId\":\"" + event.getSessionId() + "\"" +
        "}";
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send("checkin-details", event.getSessionId().toString());
        future.whenComplete((r, e) -> {
            if (e == null) {
                logger.info("Sent message=[" + event.getId() + "] with offset=[" + r.getRecordMetadata().offset() + "]");
            } else {
                logger.error("Unable to send message=[" + event.getId() + "] due to : " + e.getMessage());
            }
        });
    }
}