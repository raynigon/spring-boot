package com.raynigon.ecs.logging.application.processor

import ch.qos.logback.classic.spi.ILoggingEvent
import com.raynigon.ecs.logging.LoggingConstants
import com.raynigon.ecs.logging.application.event.EcsApplicationLogEvent
import spock.lang.Specification
import spock.lang.Subject

class MdcPropertyProcessorSpec extends Specification {

    @Subject
    MdcPropertyProcessor processor = new MdcPropertyProcessor()

    def 'write transaction id to log event'() {
        given:
        EcsApplicationLogEvent input = EcsApplicationLogEvent.builder().build()
        ILoggingEvent event = Mock()
        event.getMDCPropertyMap() >> Map.of(LoggingConstants.TRANSACTION_ID_PROPERTY, "123")

        when:
        EcsApplicationLogEvent result = processor.process(input, event)

        then:
        result != null
        result.transactionId == "123"
    }

    def 'write session id to log event'() {
        given:
        EcsApplicationLogEvent input = EcsApplicationLogEvent.builder().build()
        ILoggingEvent event = Mock()
        event.getMDCPropertyMap() >> Map.of(LoggingConstants.SESSION_ID_PROPERTY, "123")

        when:
        EcsApplicationLogEvent result = processor.process(input, event)

        then:
        result != null
        result.sessionId == "123"
    }

    def 'write labels to log event'() {
        given:
        EcsApplicationLogEvent input = EcsApplicationLogEvent.builder().build()
        ILoggingEvent event = Mock()
        event.getMDCPropertyMap() >> Map.of("my-label", "123")

        when:
        EcsApplicationLogEvent result = processor.process(input, event)

        then:
        result != null
        result.labels.containsKey("my-label")
        result.labels["my-label"] == "123"
    }
}
