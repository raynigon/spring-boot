package com.raynigon.ecs.logging.kafka.producer;


import com.raynigon.ecs.logging.LoggingConstants;
import com.raynigon.ecs.logging.kafka.ApplicationNameProvider;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Headers;
import org.slf4j.MDC;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

public class EcsProducerInterceptor<K, V> implements ProducerInterceptor<K, V> {

    private String producerName = "unknown";

    @Override
    public void configure(Map<String, ?> configs) {
        Object value = configs.get(EcsProducerConfigs.PRODUCER_NAME_CONFIG);
        if (value instanceof String) {
            producerName = (String) value;
            return;
        }
        producerName = ApplicationNameProvider.getApplicationName();
    }

    @Override
    public void close() {
        // Nothing has to be done here
    }

    @Override
    public ProducerRecord<K, V> onSend(ProducerRecord<K, V> record) {
        String transactionId = MDC.get(LoggingConstants.TRANSACTION_ID_PROPERTY);
        if (transactionId == null) {
            transactionId = UUID.randomUUID().toString();
        }
        Headers headers = record.headers();
        headers.add(LoggingConstants.KAFKA_PRODUCER_NAME_HEADER, producerName.getBytes(StandardCharsets.UTF_8));
        headers.add(LoggingConstants.KAFKA_TRANSACTION_ID_HEADER, transactionId.getBytes(StandardCharsets.UTF_8));
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        // Nothing has to be done here
    }
}
