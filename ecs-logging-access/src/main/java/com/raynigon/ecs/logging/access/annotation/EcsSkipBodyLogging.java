package com.raynigon.ecs.logging.access.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a Spring MVC handler method (or every handler method of a {@code @Controller})
 * as exempt from request/response body export, while the access log entry itself is
 * still written. Only has an effect when {@code raynigon.logging.access.export-body} is
 * enabled.
 *
 * <p>Use this for endpoints whose bodies should not appear in the access log at all,
 * e.g. because they carry sensitive data (credentials, tokens) or content that isn't
 * meaningfully representable as text (file uploads).
 *
 * @see EcsSkipAccessLogging
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface EcsSkipBodyLogging {
}
