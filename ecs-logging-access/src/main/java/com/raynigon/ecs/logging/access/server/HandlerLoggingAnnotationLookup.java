package com.raynigon.ecs.logging.access.server;

import com.raynigon.ecs.logging.access.annotation.EcsSkipAccessLogging;
import com.raynigon.ecs.logging.access.annotation.EcsSkipBodyLogging;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.Annotation;

/**
 * Resolves the {@link EcsSkipBodyLogging} / {@link EcsSkipAccessLogging} annotations for
 * the handler method that will end up serving the current request, by asking
 * {@link RequestMappingHandlerMapping} directly which method it would dispatch to,
 * without performing an actual dispatch. Handler resolution only inspects the request
 * line and headers (path, method, {@code consumes}/{@code produces}), never the body, so
 * this is safe to call before the body has been read.
 *
 * <p>{@code RequestMappingHandlerMapping} - rather than the more general
 * {@code HandlerMappingIntrospector} (deprecated for removal as of Spring Framework 7.0)
 * - is used directly: it is the only {@code HandlerMapping} that can ever produce a
 * {@link HandlerMethod} carrying these annotations in the first place, so aggregating
 * across every registered {@code HandlerMapping} (static resources, WebSockets, ...)
 * would add nothing.
 *
 * <p>Resolution can legitimately fail to produce an annotation-bearing handler - for
 * requests routed outside {@code @Controller} beans (static resources, WebSockets,
 * security filter endpoints, functional {@code RouterFunction} endpoints), or when no
 * {@link RequestMappingHandlerMapping} is available at all. In every such case this
 * class fails open, returning {@link LoggingDecision#NONE} rather than throwing, so that
 * annotation resolution never turns into an availability problem for logging itself.
 */
class HandlerLoggingAnnotationLookup {

    @Nullable
    private final RequestMappingHandlerMapping handlerMapping;

    HandlerLoggingAnnotationLookup(@Nullable RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    LoggingDecision resolve(HttpServletRequest request) {
        if (handlerMapping == null) {
            return LoggingDecision.NONE;
        }
        try {
            HandlerExecutionChain chain = handlerMapping.getHandler(request);
            if (chain == null || !(chain.getHandler() instanceof HandlerMethod handlerMethod)) {
                return LoggingDecision.NONE;
            }
            boolean skipAccessLogging = hasAnnotation(handlerMethod, EcsSkipAccessLogging.class);
            boolean skipBodyLogging = skipAccessLogging || hasAnnotation(handlerMethod, EcsSkipBodyLogging.class);
            return new LoggingDecision(skipBodyLogging, skipAccessLogging);
        } catch (Exception e) {
            // No matching handler, or handler resolution isn't possible yet for this
            // request (e.g. Content-Type/Accept condition mismatch). Fail open.
            return LoggingDecision.NONE;
        }
    }

    private boolean hasAnnotation(HandlerMethod handlerMethod, Class<? extends Annotation> annotationType) {
        return AnnotatedElementUtils.hasAnnotation(handlerMethod.getMethod(), annotationType)
                || AnnotatedElementUtils.hasAnnotation(handlerMethod.getBeanType(), annotationType);
    }

    record LoggingDecision(boolean skipBody, boolean skipAccessLogging) {
        static final LoggingDecision NONE = new LoggingDecision(false, false);
    }
}
