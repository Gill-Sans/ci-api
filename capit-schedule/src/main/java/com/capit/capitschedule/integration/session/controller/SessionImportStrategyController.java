package com.capit.capitschedule.integration.session;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/session/import")
@RequiredArgsConstructor
public class SessionImportStrategyController {
    private final SessionImportStrategyRegistry strategyRegistry;

    @GetMapping
    public ResponseEntity<List<SessionsImportStrategyDto>> getStrategies() {
        List<SessionsImportStrategyDto> strategies = strategyRegistry.getAvailableStrategies();
        return ResponseEntity.ok(strategies);
    }

}
