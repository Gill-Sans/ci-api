package com.capit.capitschedule.integration.session.service;

import com.capit.capitschedule.integration.session.dto.SessionsImportStrategyDto;
import com.capit.capitschedule.integration.session.strategy.SessionImportMetadata;
import com.capit.capitschedule.integration.session.strategy.SessionImportStrategy;
import com.capit.capitschedule.integration.session.strategy.SessionImportStrategyType;
import com.capit.capitschedule.integration.session.strategy.adapter.AdapterSessionImportStrategy;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SessionImportStrategyRegistry {

    private final ApplicationContext applicationContext;

    public SessionImportStrategyRegistry(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Returns all available strategies implementing the specified type.
     * If no type is specified, returns all strategies.
     *
     * @param strategyType Optional - the specific strategy interface or class.
     * @return A list of strategy DTOs
     */
    public <T extends SessionImportStrategy> List<SessionsImportStrategyDto> getAvailableStrategies(Class<T> strategyType) {
        return applicationContext.getBeansOfType(strategyType)
                .values()
                .stream()
                .filter(strategy -> strategy.getClass().isAnnotationPresent(SessionImportMetadata.class))
                .map(strategy -> {
                    SessionImportMetadata metadata = strategy.getClass().getAnnotation(SessionImportMetadata.class);
                    return new SessionsImportStrategyDto(metadata.strategy(), metadata.displayName());
                })
                .collect(Collectors.toList());
    }

    /**
     * Returns all available strategies.
     */
    public List<SessionsImportStrategyDto> getAvailableStrategies() {
        return getAvailableStrategies(SessionImportStrategy.class);
    }

    /**
     * Get all available adapter strategies.
     */
    public List<SessionsImportStrategyDto> getAvailableAdapters() {
        return getAvailableStrategies(AdapterSessionImportStrategy.class);
    }

    /**
     * Find a strategy using either its type or display name.
     * 
     * @param strategyType The type of strategy (can be null if using displayName)
     * @param displayName The display name of the strategy (can be null if using strategyType)
     * @return The found strategy or null if none matches
     */
    public SessionImportStrategy findStrategy(SessionImportStrategyType strategyType, String displayName) {
        Map<String, SessionImportStrategy> strategies = applicationContext.getBeansOfType(SessionImportStrategy.class);
        
        return strategies.values().stream()
                .filter(strategy -> {
                    if (!strategy.getClass().isAnnotationPresent(SessionImportMetadata.class)) {
                        return false;
                    }
                    
                    SessionImportMetadata metadata = strategy.getClass().getAnnotation(SessionImportMetadata.class);
                    
                    // If displayName is specified, match by name
                    if (displayName != null && !displayName.isEmpty()) {
                        return metadata.displayName().equals(displayName);
                    }
                    
                    // Otherwise match by type
                    return metadata.strategy() == strategyType;
                })
                .findFirst()
                .orElse(null);
    }
}
