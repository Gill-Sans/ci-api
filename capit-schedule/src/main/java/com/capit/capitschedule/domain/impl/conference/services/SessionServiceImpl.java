package com.capit.capitschedule.domain.impl.conference.services;

import com.capit.capitschedule.domain.impl.conference.commands.ImportSessionsCommand;
import com.capit.capitschedule.domain.impl.conference.dto.ImportSessionsRequest;
import com.capit.capitschedule.domain.impl.conference.dto.SessionDto;
import com.capit.capitschedule.domain.impl.conference.dto.SessionPreviewDto;
import com.capit.capitschedule.integration.session.provider.SessionDataProvider;
import com.capit.capitschedule.integration.session.provider.SessionDataRequest;
import com.capit.capitschedule.integration.session.service.SessionImportStrategyRegistry;
import com.capit.capitschedule.integration.session.strategy.SessionImportStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionServiceImpl implements SessionService {
    private final SessionDataProvider sessionDataProvider;
    private final SessionImportStrategyRegistry strategyRegistry;
    private final CommandGateway commandGateway;
    private final ObjectMapper objectMapper;

    @Override
    public CompletableFuture<List<SessionPreviewDto>> prepareSessionImport(ImportSessionsRequest request, UUID conferenceId) {
        try {
            List<Map<String, Object>> rawData = fetchExternalData(request, conferenceId);
            
            SessionImportStrategy strategy = strategyRegistry.findStrategy(
                request.getStrategyType(), 
                request.getStrategyName()
            );
            
            if (strategy == null) {
                String errorMsg = "No strategy found for: " + request.getStrategyType() + 
                                 (request.getStrategyName() != null ? " with name " + request.getStrategyName() : "");
                throw new IllegalArgumentException(errorMsg);
            }

            List<SessionDto> sessionDtos = processWithStrategy(strategy, conferenceId, rawData);
            
            List<SessionPreviewDto> previewDtos = sessionDtos.stream()
                .map(dto -> new SessionPreviewDto(
                    dto.getTitle(),
                    dto.getDescription(),
                    dto.getStartTime(),
                    dto.getEndTime(),
                    dto.getSpeaker(),
                    dto.getAddress(),
                    dto.getLocationDetails()
                ))
                .collect(Collectors.toList());
            
            CompletableFuture<List<SessionPreviewDto>> future = new CompletableFuture<>();
            future.complete(previewDtos);
            return future;
        } catch (Exception e) {
            log.error("Error preparing session import", e);
            CompletableFuture<List<SessionPreviewDto>> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }
    
    @Override
    public CompletableFuture<Void> createSessions(UUID conferenceId, List<SessionPreviewDto> sessions) {
        try {
            List<SessionDto> sessionDtos = sessions.stream()
                .map(SessionPreviewDto::toSessionDto)
                .collect(Collectors.toList());
                
            return commandGateway.send(new ImportSessionsCommand(conferenceId, sessionDtos));
        } catch (Exception e) {
            log.error("Error creating sessions", e);
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }
    
    private List<Map<String, Object>> fetchExternalData(ImportSessionsRequest request, UUID conferenceId) {
        SessionDataRequest dataRequest = new SessionDataRequest(
            conferenceId.toString(),
            request.getUrl(),
            request.getHttpMethod(),
            request.getAdditionalParams()
        );
        return sessionDataProvider.fetchSessionData(dataRequest);
    }
    
    private List<SessionDto> processWithStrategy(
            SessionImportStrategy strategy, 
            UUID conferenceId, 
            List<Map<String, Object>> rawData) {
        try {
            String externalApiPayload = objectMapper.writeValueAsString(rawData);
            return strategy.importSessions(conferenceId.toString(), externalApiPayload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process data with strategy", e);
        }
    }
}
