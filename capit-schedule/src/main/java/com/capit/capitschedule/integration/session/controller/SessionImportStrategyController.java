package com.capit.capitschedule.integration.session.controller;

import com.capit.capitschedule.integration.session.dto.SessionsImportStrategyDto;
import com.capit.capitschedule.integration.session.service.SessionImportStrategyRegistry;
import com.capit.capitschedule.integration.session.strategy.SessionImportStrategyType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/schedule/session/import")
@RequiredArgsConstructor
public class SessionImportStrategyController {
    private final SessionImportStrategyRegistry strategyRegistry;

    @GetMapping
    public ResponseEntity<List<SessionsImportStrategyDto>> getStrategies(
            @RequestParam(value = "type", required = false) String type) {
        
        if (type == null || type.isEmpty()) {
            return ResponseEntity.ok(strategyRegistry.getAvailableStrategies());
        }
        
        try {
            SessionImportStrategyType strategyType = SessionImportStrategyType.valueOf(type.toUpperCase());
            if (strategyType == SessionImportStrategyType.ADAPTER) {
                return ResponseEntity.ok(strategyRegistry.getAvailableAdapters());
            }
            return ResponseEntity.ok(strategyRegistry.getAvailableStrategies());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(strategyRegistry.getAvailableStrategies());
        }
    }
    
    @GetMapping("/adapters")
    public ResponseEntity<List<SessionsImportStrategyDto>> getAdapters() {
        return ResponseEntity.ok(strategyRegistry.getAvailableAdapters());
    }
}