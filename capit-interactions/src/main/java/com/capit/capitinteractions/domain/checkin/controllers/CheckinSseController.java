package com.capit.capitinteractions.domain.checkin.controllers;

import com.capit.capitinteractions.domain.checkin.events.CheckinEvent;
import com.capit.capitinteractions.domain.checkin.service.CheckinService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequiredArgsConstructor
public class CheckinSseController {

    private final CheckinService checkinService;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @GetMapping(value = "/api/interaction/check-ins/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamAllCheckins() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        
        // Add emitter to the list
        emitters.add(emitter);
        
        // Remove emitter on completion or timeout
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        
        return emitter;
    }

    @EventListener
    public void handleCheckinEvent(CheckinEvent event) {
        String sessionId = event.getSessionId();
        int count = event.getCount();
        
        String eventData = String.format("{\"sessionId\":\"%s\",\"count\":%d}", sessionId, count);

        List<SseEmitter> deadEmitters = new CopyOnWriteArrayList<>();
        
        emitters.forEach(emitter -> {
            try {
                emitter.send(
                    SseEmitter.event()
                        .name("checkin")
                        .data(eventData)
                );
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        });
        
        // Clean up any dead emitters
        if (!deadEmitters.isEmpty()) {
            emitters.removeAll(deadEmitters);
        }
    }
}
