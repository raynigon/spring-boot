package com.raynigon.ecs.logging.kafka.consumer;

import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.MDC;

import java.util.Map;
import java.util.UUID;

import static com.raynigon.ecs.logging.LoggingConstants.*;

public class EcsConsumerInterceptor<K, V> implements ConsumerInterceptor<K, V> {


    @Override
    @SneakyThrows
    public void configure(Map<String, ?> configs) {
        // Nothing has to be done here
    }

    @Override
    public void close() {
        // Nothing has to be done here
    }

    @Override
    public void onCommit(Map<TopicPartition, OffsetAndMetadata> offsets) {
        MDC.remove(TRANSACTION_ID_PROPERTY);
        MDC.remove(KAFKA_TOPIC_PROPERTY);
        MDC.remove(KAFKA_KAFKA_KEY_PROPERTY);
    }

    @Override
    public ConsumerRecords<K, V> onConsume(ConsumerRecords<K, V> records) {
        // No detailed logging is possible for multiple records
        if (records.count() > 1) {
            MDC.put(TRANSACTION_ID_PROPERTY, UUID.randomUUID().toString());
            return records;
        }
        ConsumerRecord<K, V> record = records.iterator().next();
        // Add MDC Tags and debug log for traceability
        MDC.put(KAFKA_TOPIC_PROPERTY, record.topic());
        if (record.key() != null) {
            MDC.put(KAFKA_KAFKA_KEY_PROPERTY, record.key().toString());
        }
        return records;
    }
}
