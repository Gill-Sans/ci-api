package com.capit.capitinteractions.domain.user;

import com.capit.events.UserEvent;
import com.capit.events.UserEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserEventConsumer {

    private final UserRepository userRepository;
    private final String USER_EVENTS_TOPIC = "user-events";
    
    @KafkaListener(topics = USER_EVENTS_TOPIC, groupId = "${spring.kafka.consumer.group-id:capit}")
    public void consumeUserEvents(UserEvent event) {
        log.info("Received user event: {}", event);
        
        if (UserEventType.USER_CREATED.equals(event.getEventType())) {
            handleUserCreatedEvent(event);
        }
    }
    
    private void handleUserCreatedEvent(UserEvent event) {
        User user = User.builder()
                .id(event.getUserId())
                .firstName(event.getFirstName())
                .lastName(event.getLastName())
                .build();
        
        userRepository.save(user);
        log.info("User created locally in interactions service: {}", user);
    }
} 