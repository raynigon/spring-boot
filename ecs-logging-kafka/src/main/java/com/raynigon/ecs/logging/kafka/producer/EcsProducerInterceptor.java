package com.raynigon.ecs.logging.kafka.producer;


import com.raynigon.ecs.logging.kafka.ApplicationNameProvider;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Headers;
import org.slf4j.MDC;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.raynigon.ecs.logging.LoggingConstants.*;

public class EcsProducerInterceptor<K, V> implements ProducerInterceptor<K, V> {

    private String producerNameHolder = null;

    @Override
    public void configure(Map<String, ?> configs) {
        Object value = configs.get(EcsProducerConfigs.PRODUCER_NAME_CONFIG);
        if (value instanceof String) {
            producerNameHolder = (String) value;
        } else {
            producerNameHolder = ApplicationNameProvider.getApplicationName();
        }
    }

    @Override
    public void close() {
        // Nothing has to be done here
    }

    @Override
    public ProducerRecord<K, V> onSend(ProducerRecord<K, V> record) {
        String producerName = getProducerName();
        String transactionId = MDC.get(TRANSACTION_ID_PROPERTY);
        if (transactionId == null) {
            transactionId = UUID.randomUUID().toString();
        }
        Headers headers = record.headers();
        if (headers.lastHeader(KAFKA_PRODUCER_NAME_HEADER) == null) {
            headers.add(KAFKA_PRODUCER_NAME_HEADER, producerName.getBytes(StandardCharsets.UTF_8));
        }
        if (headers.lastHeader(KAFKA_TRANSACTION_ID_HEADER) == null) {
            headers.add(KAFKA_TRANSACTION_ID_HEADER, transactionId.getBytes(StandardCharsets.UTF_8));
        }
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        // Nothing has to be done here
    }

    public String getProducerName() {
        if (producerNameHolder != null) {
            return producerNameHolder;
        }
        producerNameHolder = ApplicationNameProvider.getApplicationName();
        return Objects.requireNonNullElse(producerNameHolder, "unknown");
    }
}
