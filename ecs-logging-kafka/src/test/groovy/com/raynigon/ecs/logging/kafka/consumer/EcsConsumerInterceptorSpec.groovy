package com.raynigon.ecs.logging.kafka.consumer

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.header.internals.RecordHeader
import org.slf4j.MDC
import spock.lang.Specification

import java.nio.charset.StandardCharsets

import static com.raynigon.ecs.logging.LoggingConstants.*

class EcsConsumerInterceptorSpec extends Specification {

    EcsConsumerInterceptor interceptor = new EcsConsumerInterceptor();

    def setup() {
        MDC.setContextMap([:])
        MDC.clear()
    }

    def cleanup() {
        MDC.clear()
    }

    def "mdc tags on consume single message"() {
        given:
        ConsumerRecord record = new ConsumerRecord("test-topic", 0, 0, "test-key", "empty")
        record.headers().add(
                new RecordHeader(KAFKA_TRANSACTION_ID_HEADER, "transaction-id-001".getBytes(StandardCharsets.UTF_8))
        )

        and:
        ConsumerRecords records = new ConsumerRecords(
                [new TopicPartition("test-topic", 0): [record]]
        )

        when:
        interceptor.onConsume(records)

        then:
        MDC.get(KAFKA_TOPIC_PROPERTY) == "test-topic"
        MDC.get(KAFKA_KEY_PROPERTY) == "test-key"
        MDC.get(TRANSACTION_ID_PROPERTY) == "transaction-id-001"
    }

    def "mdc tags on consume single message without key"() {
        given:
        ConsumerRecord record = new ConsumerRecord("test-topic", 0, 0, null, "empty")
        record.headers().add(
                new RecordHeader(KAFKA_TRANSACTION_ID_HEADER, "transaction-id-001".getBytes(StandardCharsets.UTF_8))
        )

        and:
        ConsumerRecords records = new ConsumerRecords(
                [new TopicPartition("test-topic", 0): [record]]
        )

        when:
        interceptor.onConsume(records)

        then:
        MDC.get(KAFKA_TOPIC_PROPERTY) == "test-topic"
        MDC.get(KAFKA_KEY_PROPERTY) == null
        MDC.get(TRANSACTION_ID_PROPERTY) == "transaction-id-001"
    }

    def "mdc tags on consume single message without transaction id"() {
        given:
        ConsumerRecord record = new ConsumerRecord("test-topic", 0, 0, "test-key", "empty")

        and:
        ConsumerRecords records = new ConsumerRecords(
                [new TopicPartition("test-topic", 0): [record]]
        )

        when:
        interceptor.onConsume(records)

        then:
        MDC.get(KAFKA_TOPIC_PROPERTY) == "test-topic"
        MDC.get(KAFKA_KEY_PROPERTY) == "test-key"
        UUID.fromString(MDC.get(TRANSACTION_ID_PROPERTY)) != null
    }

    def "mdc tags on consume batch"() {
        given:
        ConsumerRecord record0 = new ConsumerRecord("test-topic", 0, 0, "key-000", "empty")
        record0.headers().add(new RecordHeader(KAFKA_TRANSACTION_ID_HEADER, "transaction-id-000".getBytes(StandardCharsets.UTF_8)))
        ConsumerRecord record1 = new ConsumerRecord("test-topic", 0, 1, "key-001", "empty")
        record1.headers().add(new RecordHeader(KAFKA_TRANSACTION_ID_HEADER, "transaction-id-001".getBytes(StandardCharsets.UTF_8)))

        and:
        ConsumerRecords records = new ConsumerRecords(
                [new TopicPartition("test-topic", 0): [record0, record1]]
        )

        when:
        interceptor.onConsume(records)

        then:
        UUID.fromString(MDC.get(TRANSACTION_ID_PROPERTY)) != null
        MDC.get(KAFKA_TOPIC_PROPERTY) == null
        MDC.get(KAFKA_KEY_PROPERTY) == null
    }
}
