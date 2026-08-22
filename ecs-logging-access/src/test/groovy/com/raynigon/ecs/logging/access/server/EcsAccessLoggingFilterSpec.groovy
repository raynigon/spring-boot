package com.raynigon.ecs.logging.access.server

import com.raynigon.ecs.logging.access.AccessLogProperties
import com.raynigon.ecs.logging.access.helper.FakeServletInputStream
import jakarta.servlet.FilterChain
import jakarta.servlet.FilterConfig
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import spock.lang.Specification

class EcsAccessLoggingFilterSpec extends Specification {

    def "bypasses teeing for a multipart request when excludeMultipartBody is enabled"() {
        given:
        AccessLogProperties properties = new AccessLogProperties()
        properties.setExcludeMultipartBody(true)
        EcsAccessLoggingFilter filter = activeFilter(properties, HandlerLoggingAnnotationLookup.LoggingDecision.NONE)

        HttpServletRequest request = Mock()
        request.getContentType() >> "multipart/form-data; boundary=abc"
        HttpServletResponse response = Mock()
        FilterChain chain = Mock()

        when:
        filter.doFilter(request, response, chain)

        then: "the original, unwrapped request/response reach the chain"
        1 * chain.doFilter({ it.is(request) }, { it.is(response) })
    }

    def "still tees a multipart request when excludeMultipartBody is disabled"() {
        given:
        AccessLogProperties properties = new AccessLogProperties()
        properties.setExcludeMultipartBody(false)
        EcsAccessLoggingFilter filter = activeFilter(properties, HandlerLoggingAnnotationLookup.LoggingDecision.NONE)

        HttpServletRequest request = Mock()
        request.getContentType() >> "multipart/form-data; boundary=abc"
        request.getMethod() >> "POST"
        request.getInputStream() >> new FakeServletInputStream()
        HttpServletResponse response = Mock()
        FilterChain chain = Mock()

        when:
        filter.doFilter(request, response, chain)

        then: "the request reaching the chain is wrapped, not the original instance"
        1 * chain.doFilter({ !it.is(request) }, _)
    }

    def "bypasses teeing for a non-multipart request when annotated with @EcsSkipBodyLogging"() {
        given:
        AccessLogProperties properties = new AccessLogProperties()
        properties.setExcludeMultipartBody(false)
        def decision = new HandlerLoggingAnnotationLookup.LoggingDecision(true, false)
        EcsAccessLoggingFilter filter = activeFilter(properties, decision)

        HttpServletRequest request = Mock()
        request.getContentType() >> "application/json"
        HttpServletResponse response = Mock()
        FilterChain chain = Mock()

        when:
        filter.doFilter(request, response, chain)

        then:
        1 * chain.doFilter({ it.is(request) }, { it.is(response) })
        0 * request.setAttribute(EcsLoggingRequestAttributes.SKIP_ACCESS_LOGGING, _)
    }

    def "marks the request for access-log skipping when annotated with @EcsSkipAccessLogging"() {
        given:
        AccessLogProperties properties = new AccessLogProperties()
        def decision = new HandlerLoggingAnnotationLookup.LoggingDecision(true, true)
        EcsAccessLoggingFilter filter = activeFilter(properties, decision)

        HttpServletRequest request = Mock()
        request.getContentType() >> "application/json"
        HttpServletResponse response = Mock()
        FilterChain chain = Mock()

        when:
        filter.doFilter(request, response, chain)

        then:
        1 * request.setAttribute(EcsLoggingRequestAttributes.SKIP_ACCESS_LOGGING, Boolean.TRUE)
        1 * chain.doFilter({ it.is(request) }, { it.is(response) })
    }

    def "tees a plain, unannotated non-multipart request as before"() {
        given:
        AccessLogProperties properties = new AccessLogProperties()
        EcsAccessLoggingFilter filter = activeFilter(properties, HandlerLoggingAnnotationLookup.LoggingDecision.NONE)

        HttpServletRequest request = Mock()
        request.getContentType() >> "application/json"
        request.getMethod() >> "POST"
        request.getInputStream() >> new FakeServletInputStream('{"a":1}'.bytes)
        HttpServletResponse response = Mock()
        FilterChain chain = Mock()

        when:
        filter.doFilter(request, response, chain)

        then: "the request reaching the chain is wrapped, not the original instance"
        1 * chain.doFilter({ !it.is(request) }, _)
    }

    private EcsAccessLoggingFilter activeFilter(
            AccessLogProperties properties, HandlerLoggingAnnotationLookup.LoggingDecision decision) {
        HandlerLoggingAnnotationLookup lookup = Stub()
        lookup.resolve(_) >> decision
        EcsAccessLoggingFilter filter = new EcsAccessLoggingFilter(properties, lookup)
        // TeeFilter only tees when `active`, which init() computes from init-params.
        // Without a container-driven init() call `active` defaults to false, which
        // would make every request bypass teeing regardless of our own logic - so the
        // tests that need to observe real teeing behaviour must activate it first.
        FilterConfig filterConfig = Mock()
        filterConfig.getInitParameter(_) >> null
        filter.init(filterConfig)
        return filter
    }
}
