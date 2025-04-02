package com.capit.capitschedule.integration.session;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionImportStrategyRegistry {

    private final ApplicationContext applicationContext;

    public SessionImportStrategyRegistry(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public List<SessionsImportStrategyDto> getAvailableStrategies() {
        return applicationContext.getBeansOfType(SessionImportStrategy.class)
                .values()
                .stream()
                .filter(strategy -> strategy.getClass().isAnnotationPresent(SessionImportMetadata.class))
                .map(strategy -> {
                    SessionImportMetadata metadata = strategy.getClass().getAnnotation(SessionImportMetadata.class);
                    return new SessionsImportStrategyDto(metadata.strategy(), metadata.displayName());
                })
                .collect(Collectors.toList());
    }
}
