package com.capit.capitschedule.integration.session.strategy;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SessionImportMetadata {
    SessionImportStrategyType strategy();
    String displayName();
}
