package com.capit.capitinteractions.domain.impl.checkin.services;

import com.capit.capitinteractions.domain.impl.checkin.events.CheckinCreatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class CheckinKafkaPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public CheckinKafkaPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @EventHandler
    public void on(CheckinCreatedEvent event) {
        kafkaTemplate.send("checkin-details", event.getId().toString());
    }
}