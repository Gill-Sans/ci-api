package com.capit.capitusers.user.services;

import com.capit.capitusers.user.entities.User;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.capit.events.UserEvent;
import com.capit.events.UserEventType;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserEventProducer {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String USER_EVENTS_TOPIC = "user-events";
    
    public void publishUserCreatedEvent(User user) {
        UserEvent event = UserEvent.builder()
                .eventType(UserEventType.USER_CREATED)
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
        
        log.info("Publishing user created event: {}", event);
        kafkaTemplate.send(USER_EVENTS_TOPIC, user.getId().toString(), event);
    }
} 