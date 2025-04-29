package com.capit.capitinteractions.domain.checkin.handlers;

import com.capit.capitinteractions.domain.checkin.events.CheckinKafkaEvent;
import com.capit.capitinteractions.domain.checkin.requests.CreateCheckinRequest;
import com.capit.capitinteractions.domain.checkin.service.CheckinService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;
import reactor.core.publisher.Sinks;
import com.capit.capitinteractions.domain.checkin.entity.Checkin;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.capit.capitinteractions.domain.user.UserRepository;
import com.capit.capitinteractions.domain.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CheckinWebSocketHandler implements WebSocketHandler {

    private final CheckinService checkinService;
    private final ObjectMapper objectMapper;
    private final KafkaSender<String, CheckinKafkaEvent> kafkaSender;
    private final Sinks.Many<CheckinKafkaEvent> checkinSink;
    private final UserRepository userRepository;
    private final String instanceId;

    @Override
    public @NotNull Mono<Void> handle(@NotNull WebSocketSession session) {
        String cid = extractConferenceId(session);
        log.debug("🟢 WebSocket handle start for conferenceId={}", cid);

        Mono<WebSocketMessage> initial = initialSnapshot(session, cid)
                .doOnNext(msg -> log.debug("➡️ sending initial snapshot: {}", msg.getPayloadAsText()))
                .doOnError(err -> log.error("❌ Failed to build INITIAL_SNAPSHOT", err))
                .onErrorResume(err -> {
                    // fallback empty snapshot so we stay alive
                    String fallback = "{\"eventType\":\"INITIAL_SNAPSHOT\",\"conferenceId\":\"" + cid + "\",\"checkins\":[]}";
                    log.warn("⚠️ Fallback initial snapshot: {}", fallback);
                    return Mono.just(session.textMessage(fallback));
                });

        Flux<WebSocketMessage> events = eventFlux(session, cid)
                .doOnNext(msg -> log.debug("➡️ sending event: {}", msg.getPayloadAsText()))
                .doOnError(err -> log.error("❌ Error in eventFlux", err))
                .onErrorContinue((err, obj) -> log.warn("⚠️ Dropping bad event: {} because {}", obj, err.getMessage()));

        Mono<Void> send = session.send(Flux.concat(initial, events))
                .doOnError(err -> log.error("❌ WebSocket send pipeline error", err));

        Mono<Void> receive = session.receive()
                .doOnError(err -> log.error("❌ WebSocket receive pipeline error", err))
                .map(WebSocketMessage::getPayloadAsText)
                .flatMap(this::parseRequest)
                .flatMap(this::handleRequest)
                .then();

        return Mono.when(send, receive)
                .doOnError(err -> log.error("❌ overall WebSocket error", err))
                .doOnSuccess(v -> log.debug("🔴 WebSocket handler terminates cleanly"));
    }

    private String extractConferenceId(WebSocketSession session) {
        URI uri = session.getHandshakeInfo().getUri();
        MultiValueMap<String, String> params = UriComponentsBuilder.fromUri(uri).build().getQueryParams();
        return params.getFirst("conferenceId");
    }

    private Mono<WebSocketMessage> initialSnapshot(WebSocketSession session, String conferenceId) {
        return Mono.fromCallable(() -> buildInitialPayload(conferenceId))
                .subscribeOn(Schedulers.boundedElastic())
                .map(session::textMessage);
    }

    private String buildInitialPayload(String conferenceId) throws JsonProcessingException {
        List<Map<String, Object>> entries = checkinService
                .getCheckinsByConferenceId(UUID.fromString(conferenceId))
                .stream()
                .map(this::toSnapshotEntry)
                .toList();
        Map<String, Object> envelope = Map.of(
                "eventType", "INITIAL_SNAPSHOT",
                "conferenceId", conferenceId,
                "checkins", entries
        );
        return objectMapper.writeValueAsString(envelope);
    }

    private Map<String, Object> toSnapshotEntry(Checkin chk) {
        User user = userRepository.findById(chk.getUserId()).orElse(null);
        // Ensure sessionId is never null: represent missing sessionId as empty string
        String sessionId = chk.getSessionId() != null ? chk.getSessionId().toString() : "";
        return Map.of(
                "userId", chk.getUserId().toString(),
                "firstName", user != null ? user.getFirstName() : "",
                "lastName", user != null ? user.getLastName() : "",
                "sessionId", sessionId
        );
    }

    private Flux<WebSocketMessage> eventFlux(WebSocketSession session, String conferenceId) {
        return checkinSink.asFlux()
                .filter(evt -> evt.getConferenceId().equals(conferenceId))
                .flatMap(evt -> {
                    try {
                        String json = objectMapper.writeValueAsString(evt);
                        return Mono.just(session.textMessage(json));
                    } catch (JsonProcessingException e) {
                        log.error("Failed to serialize event {}", evt, e);
                        return Mono.empty();
                    }
                })
                .doOnError(err -> log.error("Error in event flux", err));
    }

    private Mono<CreateCheckinRequest> parseRequest(String text) {
        return Mono.fromCallable(() -> objectMapper.readValue(text, CreateCheckinRequest.class))
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<Void> handleRequest(CreateCheckinRequest request) {
        return Mono.fromCallable(() -> checkinService.createCheckin(request))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(this::publishEvent)
                .then();
    }

    private Mono<Void> publishEvent(Checkin saved) {
        User user = userRepository.findById(saved.getUserId()).orElse(null);
        CheckinKafkaEvent event = CheckinKafkaEvent.builder()
                .sessionId(saved.getSessionId().toString())
                .conferenceId(saved.getConferenceId().toString())
                .userId(saved.getUserId().toString())
                .firstName(user != null ? user.getFirstName() : "")
                .lastName(user != null ? user.getLastName() : "")
                .instanceId(instanceId)
                .build();

        Sinks.EmitResult emitResult = checkinSink.tryEmitNext(event);
        if (emitResult.isFailure()) {
            log.warn("Local emit failed for event {} : {}", event, emitResult);
        }

        SenderRecord<String, CheckinKafkaEvent, Void> record = SenderRecord.create(
                new ProducerRecord<>("checkin-events", event.getSessionId(), event),
                null
        );
        return kafkaSender.send(Mono.just(record))
                .doOnError(err -> log.error("Kafka send failed", err))
                .then();
    }
}
