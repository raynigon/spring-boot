package com.raynigon.ecs.logging.application.processor

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.classic.spi.IThrowableProxy
import ch.qos.logback.classic.spi.StackTraceElementProxy
import ch.qos.logback.classic.spi.ThrowableProxy
import com.raynigon.ecs.logging.application.event.EcsApplicationLogEvent
import spock.lang.Specification
import spock.lang.Subject

class ThrowableProcessorSpec extends Specification {

    @Subject
    ThrowableProcessor processor = new ThrowableProcessor()

    def 'event without throwable proxy is a no op'() {
        given:
        EcsApplicationLogEvent input = EcsApplicationLogEvent.builder().build()
        ILoggingEvent event = Mock()
        event.getThrowableProxy() >> null

        when:
        EcsApplicationLogEvent result = processor.process(input, event)

        then:
        result == input
    }

    def 'event with ThrowableProxy'() {
        given:
        EcsApplicationLogEvent input = EcsApplicationLogEvent.builder().build()
        ILoggingEvent event = Mock()

        and:
        ThrowableProxy throwableProxy = Mock()
        throwableProxy.getMessage() >> "Test"
        throwableProxy.getThrowable() >> new RuntimeException()
        throwableProxy.getClassName() >> "java.lang.RuntimeException"
        event.getThrowableProxy() >> throwableProxy

        when:
        EcsApplicationLogEvent result = processor.process(input, event)

        then:
        result.errorMessage == "Test"
        result.errorType == "java.lang.RuntimeException"
        result.errorStackTrace.contains("java.lang.RuntimeException")
    }

    def 'event with IThrowableProxy'() {
        given:
        EcsApplicationLogEvent input = EcsApplicationLogEvent.builder().build()
        ILoggingEvent event = Mock()

        and:
        IThrowableProxy throwableProxy = Mock()
        throwableProxy.getMessage() >> "Test"
        throwableProxy.getStackTraceElementProxyArray() >> new StackTraceElementProxy[0]
        event.getThrowableProxy() >> throwableProxy

        when:
        EcsApplicationLogEvent result = processor.process(input, event)

        then:
        result.errorMessage == "Test"
    }
}
