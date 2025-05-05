package com.capit.capitinteractions.config;

import com.capit.capitinteractions.domain.checkin.handlers.CheckinWebsocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private final CheckinWebsocketHandler checkinWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(checkinWebSocketHandler, "/api/interactions/ws/checkins")
            .addInterceptors(new HttpSessionHandshakeInterceptor())
            .setAllowedOrigins("http://localhost:4200", "http://192.168.0.56", "http://capit.mertenshome.com");

    }
}