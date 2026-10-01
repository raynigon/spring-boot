package com.raynigon.ecs.logging.access.server

import com.raynigon.ecs.logging.access.annotation.EcsSkipAccessLogging
import com.raynigon.ecs.logging.access.annotation.EcsSkipBodyLogging
import jakarta.servlet.http.HttpServletRequest
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.support.StaticWebApplicationContext
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import spock.lang.Shared
import spock.lang.Specification

/**
 * getHandler() on RequestMappingHandlerMapping is a final template method, so it can't
 * be Spock-mocked (byte-buddy refuses to override final methods). Instead these tests
 * wire a real RequestMappingHandlerMapping against a minimal StaticWebApplicationContext
 * holding small throwaway controllers - the same pattern Spring's own handler mapping
 * tests use - and exercise HandlerLoggingAnnotationLookup against real resolution.
 */
class HandlerLoggingAnnotationLookupSpec extends Specification {

    @Shared
    RequestMappingHandlerMapping handlerMapping = buildHandlerMapping(TestController)

    def "returns NONE when no handler mapping is available"() {
        given:
        def lookup = new HandlerLoggingAnnotationLookup(null)

        when:
        def decision = lookup.resolve(request("GET", "/anything"))

        then:
        !decision.skipBody()
        !decision.skipAccessLogging()
    }

    def "returns NONE when no handler matches the request"() {
        given:
        def lookup = new HandlerLoggingAnnotationLookup(handlerMapping)

        when:
        def decision = lookup.resolve(request("GET", "/does-not-exist"))

        then:
        !decision.skipBody()
        !decision.skipAccessLogging()
    }

    def "fails open when handler resolution throws (e.g. method not supported)"() {
        given: "/skip-body is mapped POST-only, so a GET makes Spring throw " +
                "HttpRequestMethodNotSupportedException instead of returning null"
        def lookup = new HandlerLoggingAnnotationLookup(handlerMapping)

        when:
        def decision = lookup.resolve(request("GET", "/skip-body"))

        then:
        noExceptionThrown()
        !decision.skipBody()
        !decision.skipAccessLogging()
    }

    def "detects a method-level @EcsSkipBodyLogging annotation"() {
        given:
        def lookup = new HandlerLoggingAnnotationLookup(handlerMapping)

        when:
        def decision = lookup.resolve(request("POST", "/skip-body"))

        then:
        decision.skipBody()
        !decision.skipAccessLogging()
    }

    def "detects a method-level @EcsSkipAccessLogging annotation and implies skipBody"() {
        given:
        def lookup = new HandlerLoggingAnnotationLookup(handlerMapping)

        when:
        def decision = lookup.resolve(request("POST", "/skip-log"))

        then:
        decision.skipBody()
        decision.skipAccessLogging()
    }

    def "returns NONE for an unannotated method"() {
        given:
        def lookup = new HandlerLoggingAnnotationLookup(handlerMapping)

        when:
        def decision = lookup.resolve(request("POST", "/plain"))

        then:
        !decision.skipBody()
        !decision.skipAccessLogging()
    }

    def "detects a class-level @EcsSkipBodyLogging annotation"() {
        given:
        RequestMappingHandlerMapping mapping = buildHandlerMapping(AnnotatedAtClassLevelController)
        def lookup = new HandlerLoggingAnnotationLookup(mapping)

        when:
        def decision = lookup.resolve(request("POST", "/class-level"))

        then:
        decision.skipBody()
        !decision.skipAccessLogging()
    }

    private static HttpServletRequest request(String method, String path) {
        return new MockHttpServletRequest(method, path)
    }

    private static RequestMappingHandlerMapping buildHandlerMapping(Class<?> controllerType) {
        StaticWebApplicationContext context = new StaticWebApplicationContext()
        context.registerSingleton("testController", controllerType)
        context.refresh()

        RequestMappingHandlerMapping mapping = new RequestMappingHandlerMapping()
        mapping.setApplicationContext(context)
        mapping.afterPropertiesSet()
        return mapping
    }

    @RestController
    static class TestController {
        @PostMapping("/skip-body")
        @EcsSkipBodyLogging
        String skipBody() {
            return ""
        }

        @PostMapping("/skip-log")
        @EcsSkipAccessLogging
        String skipLog() {
            return ""
        }

        @PostMapping("/plain")
        String plain() {
            return ""
        }
    }

    @RestController
    @EcsSkipBodyLogging
    static class AnnotatedAtClassLevelController {
        @PostMapping("/class-level")
        String classLevel() {
            return ""
        }
    }
}
