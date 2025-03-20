package com.capit.capitinteractions.api.checkin.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class CheckinSseController {

    private final SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

    @GetMapping(value = "/checkins/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamCheckins() {
        return emitter;
    }

    public void sendCheckinUpdate(String sessionId, int newCount) {
        try {
            emitter.send(
                    SseEmitter.event()
                            .name("checkin")          // event name
                            .data("{\"sessionId\":\"" + sessionId + "\",\"count\":" + newCount + "}")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
