package com.raynigon.ecs.logging.access.server;

import ch.qos.logback.access.common.servlet.TeeFilter;
import com.raynigon.ecs.logging.access.AccessLogProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Locale;

/**
 * Replaces {@link TeeFilter} as the body-export filter registered by
 * {@link AccessLogFilterConfiguration}. {@code TeeFilter} eagerly reads a request's
 * entire raw {@code InputStream} into memory in its constructor, before the servlet
 * container has parsed it. For {@code multipart/form-data} requests this consumes the
 * stream that the container's own multipart parser (triggered later by Spring's
 * {@code @RequestPart}/{@code MultipartFile} resolution) needs to read afterwards,
 * which fails - surfacing to the client as a 500.
 *
 * <p>This filter avoids that in two ways, both computed before any body is touched
 * (handler resolution and the {@code Content-Type} check below only look at the
 * request line/headers):
 *
 * <ul>
 *   <li>{@link AccessLogProperties#isExcludeMultipartBody()} (default {@code true}):
 *       skips body-teeing for every {@code multipart/*} request.</li>
 *   <li>{@link com.raynigon.ecs.logging.access.annotation.EcsSkipBodyLogging} /
 *       {@link com.raynigon.ecs.logging.access.annotation.EcsSkipAccessLogging}: skips
 *       body-teeing (and optionally the whole access log entry) for a specific handler
 *       method, regardless of content type.</li>
 * </ul>
 *
 * <p>Skipping body-teeing here means both the request and the response body are
 * excluded from that call's access log entry - {@code TeeFilter} wraps both together in
 * one pass, and there's no supported way to tee only one side without depending on
 * {@code logback-access}'s package-private {@code Tee*} implementation classes.
 */
public class EcsAccessLoggingFilter extends TeeFilter {

    private final AccessLogProperties properties;
    private final HandlerLoggingAnnotationLookup annotationLookup;

    EcsAccessLoggingFilter(AccessLogProperties properties, HandlerLoggingAnnotationLookup annotationLookup) {
        this.properties = properties;
        this.annotationLookup = annotationLookup;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest httpRequest)) {
            super.doFilter(request, response, chain);
            return;
        }

        HandlerLoggingAnnotationLookup.LoggingDecision decision = annotationLookup.resolve(httpRequest);
        if (decision.skipAccessLogging()) {
            httpRequest.setAttribute(EcsLoggingRequestAttributes.SKIP_ACCESS_LOGGING, Boolean.TRUE);
        }

        boolean skipBody = decision.skipBody() || (properties.isExcludeMultipartBody() && isMultipart(httpRequest));
        if (skipBody) {
            chain.doFilter(request, response);
        } else {
            super.doFilter(request, response, chain);
        }
    }

    private static boolean isMultipart(HttpServletRequest request) {
        String contentType = request.getContentType();
        return contentType != null
                && contentType.toLowerCase(Locale.ROOT).startsWith(MediaType.MULTIPART_FORM_DATA_VALUE);
    }
}
