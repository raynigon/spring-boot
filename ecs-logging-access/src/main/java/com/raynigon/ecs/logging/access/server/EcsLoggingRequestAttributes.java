package com.raynigon.ecs.logging.access.server;

/**
 * Request attribute names used to pass logging decisions from {@link EcsAccessLoggingFilter}
 * (which resolves them, early in the filter chain) to
 * {@link com.raynigon.ecs.logging.access.logback.LogbackAccessValve} (which reads them when
 * the response has completed, to decide whether to emit an access log entry).
 */
public final class EcsLoggingRequestAttributes {

    /**
     * Set to {@link Boolean#TRUE} when the resolved handler is annotated with
     * {@link com.raynigon.ecs.logging.access.annotation.EcsSkipAccessLogging}. Absent
     * (rather than {@code false}) otherwise.
     */
    public static final String SKIP_ACCESS_LOGGING = EcsLoggingRequestAttributes.class.getName() + ".SKIP_ACCESS_LOGGING";

    private EcsLoggingRequestAttributes() {
    }
}
