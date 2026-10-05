package com.raynigon.ecs.logging.access.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a Spring MVC handler method (or every handler method of a {@code @Controller})
 * as exempt from access logging entirely - no entry is written for matching requests at
 * all. Implies {@link EcsSkipBodyLogging}.
 *
 * <p>This is an annotation-driven alternative to
 * {@code raynigon.logging.access.exclude-endpoints}, useful when the path contains
 * variables (e.g. {@code {id}}) that can't be expressed as the exact-match strings that
 * property requires.
 *
 * @see EcsSkipBodyLogging
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface EcsSkipAccessLogging {
}
