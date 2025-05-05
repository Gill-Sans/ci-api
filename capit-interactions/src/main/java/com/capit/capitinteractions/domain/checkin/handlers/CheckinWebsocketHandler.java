package com.capit.capitinteractions.domain.checkin.handlers;

import com.capit.capitinteractions.domain.checkin.dto.CheckinDto;
import com.capit.capitinteractions.domain.checkin.dto.CheckinEventMessage;
import com.capit.capitinteractions.domain.checkin.dto.CheckinType;
import com.capit.capitinteractions.domain.checkin.requests.CheckinRequest;
import com.capit.capitinteractions.domain.checkin.dto.EventType;
import com.capit.capitinteractions.domain.checkin.dto.InitialSnapshotEvent;
import com.capit.capitinteractions.domain.checkin.service.CheckinService;
import com.capit.capitinteractions.domain.user.User;
import com.capit.capitinteractions.domain.user.UserRepository;
import com.capit.exceptions.BaseRuntimeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Component
@RequiredArgsConstructor
public class CheckinWebsocketHandler extends TextWebSocketHandler {

    private final CheckinService checkinService;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    private final Set<WebSocketSession> sessions = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
        sessions.add(session);
        log.info("Connection established; sending initial snapshot to session {}", session.getId());
        sendInitialSnapshot(session);
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, org.springframework.web.socket.@NotNull CloseStatus status) {
        sessions.remove(session);
        log.info("Connection {} closed: {}", session.getId(), status);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        log.info("Received message from {}: {}", session.getId(), message.getPayload());
        try {
            CheckinRequest req = objectMapper.readValue(message.getPayload(), CheckinRequest.class);
            CheckinDto dto;
            if (req.type() == CheckinType.CHECK_IN) {
                log.info("Checkin request: {}", req);
                dto = checkinService.createCheckin(req);
                broadcastCheckinEvent(dto);
            } else if (req.type() == CheckinType.CHECK_OUT) {
                log.info("Checkout request: {}", req);
                // Find existing checkin for this user and session
                List<CheckinDto> checkins = checkinService.getCheckinsBySessionId(req.sessionId());
                Optional<CheckinDto> existing = checkins.stream()
                    .filter(c -> c.getUserId().equals(req.userId()))
                    .findFirst();
                if (existing.isEmpty()) {
                    log.warn("Checkin not found for user {} and session {}", req.userId(), req.sessionId());
                    return;
                }
                dto = existing.get();
                // Delete the checkin
                checkinService.deleteCheckin(dto.getId());
                // Broadcast checkout event
                broadcastCheckoutEvent(dto);
            } else {
                log.warn("Unknown checkin type: {}", req.type());
                return;
            }
        } catch (Exception e) {
            log.error("Failed to handle incoming message", e);
        }
    }

    private String extractConferenceId(WebSocketSession session) {
        URI uri = session.getUri();
        if (uri == null) {
            throw new BaseRuntimeException("WebSocket URI is null", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        MultiValueMap<String, String> params = UriComponentsBuilder.fromUri(uri).build().getQueryParams();
        return params.getFirst("conferenceId");
    }

    private void sendInitialSnapshot(WebSocketSession session) {
        try {
            UUID confId = UUID.fromString(extractConferenceId(session));
            List<CheckinDto> dtos = checkinService.getCheckinsByConferenceId(confId);
            dtos.forEach(dto -> {
                User user = userRepository.findById(dto.getUserId())
                        .orElseThrow(() -> new BaseRuntimeException("User not found with id: " + dto.getUserId(), HttpStatus.NOT_FOUND));
                dto.setFirstName(user.getFirstName());
                dto.setLastName(user.getLastName());
            });
            InitialSnapshotEvent event = new InitialSnapshotEvent(dtos);
            String payload = objectMapper.writeValueAsString(event);
            session.sendMessage(new TextMessage(payload));
        } catch (IOException e) {
            log.error("Failed to send initial snapshot", e);
        }
    }

    private void broadcastCheckinEvent(CheckinDto dto) {
        try {
            CheckinEventMessage event = new CheckinEventMessage(EventType.CHECK_IN, dto);
            String payload = objectMapper.writeValueAsString(event);
            TextMessage msg = new TextMessage(payload);
            for (WebSocketSession s : sessions) {
                if (s.isOpen()) {
                    s.sendMessage(msg);
                }
            }
        } catch (IOException e) {
            log.error("Failed to broadcast checkin event", e);
        }
    }

    private void broadcastCheckoutEvent(CheckinDto dto) {
        try {
            CheckinEventMessage event = new CheckinEventMessage(EventType.CHECK_OUT, dto);
            String payload = objectMapper.writeValueAsString(event);
            TextMessage msg = new TextMessage(payload);
            for (WebSocketSession s : sessions) {
                if (s.isOpen()) {
                    s.sendMessage(msg);
                }
            }
        } catch (IOException e) {
            log.error("Failed to broadcast checkout event", e);
        }
    }
}
