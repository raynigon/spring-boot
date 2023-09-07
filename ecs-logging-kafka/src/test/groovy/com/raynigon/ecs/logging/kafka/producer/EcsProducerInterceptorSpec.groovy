package com.raynigon.ecs.logging.kafka.producer

import com.raynigon.ecs.logging.kafka.ApplicationNameProvider
import org.apache.kafka.clients.producer.ProducerRecord
import spock.lang.Specification
import spock.lang.Subject

import static com.raynigon.ecs.logging.LoggingConstants.KAFKA_PRODUCER_NAME_HEADER
import static com.raynigon.ecs.logging.LoggingConstants.KAFKA_TRANSACTION_ID_HEADER

class EcsProducerInterceptorSpec extends Specification {

    @Subject
    EcsProducerInterceptor interceptor = new EcsProducerInterceptor()

    def "headers are set correctly"() {
        given:
        ProducerRecord record = new ProducerRecord("test-topic", null, "empty")

        and:
        ApplicationNameProvider provider = new ApplicationNameProvider()
        provider.applicationName = "test-producer"
        provider.init()

        when:
        interceptor.onSend(record)

        then:
        record.headers().lastHeader(KAFKA_PRODUCER_NAME_HEADER) != null
        record.headers().lastHeader(KAFKA_TRANSACTION_ID_HEADER) != null
    }
}
