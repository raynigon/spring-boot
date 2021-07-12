package com.raynigon.ecs.logging.access.processor

import ch.qos.logback.access.spi.IAccessEvent
import com.raynigon.ecs.logging.access.event.EcsAccessLogEvent
import spock.lang.Specification
import spock.lang.Subject

class TransactionIdProcessorSpec extends Specification {

    @Subject
    TransactionIdProcessor processor = new TransactionIdProcessor()

    def "transaction id is given"() {
        given:
        IAccessEvent event = Mock()
        event.getResponseHeader(_ as String) >> "test 123"

        and:
        EcsAccessLogEvent result = EcsAccessLogEvent.builder().build()

        when:
        result = processor.process(result, event)

        then:
        result.transactionId == "test 123"
    }
}
