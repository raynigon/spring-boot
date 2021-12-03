package com.raynigon.ecs.logging.access.processor;

import ch.qos.logback.access.spi.IAccessEvent;
import com.raynigon.ecs.logging.access.event.EcsAccessLogEvent;

import static com.raynigon.ecs.logging.LoggingConstants.TRANSACTION_ID_HEADER;

public class BodyProcessor implements AccessEventProcessor {
    @Override
    public EcsAccessLogEvent process(EcsAccessLogEvent result, IAccessEvent event) {
        return result.toBuilder()
                .requestBody(event.getRequestContent())
                .responseBody(event.getResponseContent())
                .build();
    }
}
