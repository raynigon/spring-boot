package com.raynigon.ecs.logging.access.processor

import ch.qos.logback.access.common.spi.IAccessEvent
import com.raynigon.ecs.logging.access.event.EcsAccessLogEvent
import spock.lang.Specification
import spock.lang.Subject

class SessionIdProcessorSpec extends Specification {

    @Subject
    SessionIdProcessor processor = new SessionIdProcessor()

    def "session id is missing"() {
        given:
        IAccessEvent event = Mock()
        event.getRequestHeader(_ as String) >> "-"

        and:
        EcsAccessLogEvent result = EcsAccessLogEvent.builder().build()

        when:
        result = processor.process(result, event)

        then:
        result.sessionId == null
    }

    def "session id is given"() {
        given:
        IAccessEvent event = Mock()
        event.getRequestHeader(_ as String) >> "test 123"

        and:
        EcsAccessLogEvent result = EcsAccessLogEvent.builder().build()

        when:
        result = processor.process(result, event)

        then:
        result.sessionId == "test 123"
    }
}
