package com.capit.capitschedule.integration.session;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SessionImportMetadata {
    /**
     * The strategy type, e.g., "adapter", "scrape", or "customMapping".
     */
    String strategy();

    /**
     * The display name for this strategy.
     */
    String displayName();
}
