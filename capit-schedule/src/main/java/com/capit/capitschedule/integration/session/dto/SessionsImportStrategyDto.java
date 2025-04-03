package com.capit.capitschedule.integration.session.dto;

import com.capit.capitschedule.integration.session.strategy.SessionImportStrategyType;
import java.util.Objects;

/**
 * DTO for session import strategies that can be selected by the user.
 */
public class SessionsImportStrategyDto {
    private SessionImportStrategyType strategy;
    private String displayName;
    
    // Default constructor for serialization
    public SessionsImportStrategyDto() {
    }
    
    public SessionsImportStrategyDto(SessionImportStrategyType strategy, String displayName) {
        this.strategy = strategy;
        this.displayName = displayName;
    }
    
    public SessionImportStrategyType getStrategy() {
        return strategy;
    }
    
    public void setStrategy(SessionImportStrategyType strategy) {
        this.strategy = strategy;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionsImportStrategyDto that = (SessionsImportStrategyDto) o;
        return Objects.equals(strategy, that.strategy) &&
               Objects.equals(displayName, that.displayName);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(strategy, displayName);
    }
}