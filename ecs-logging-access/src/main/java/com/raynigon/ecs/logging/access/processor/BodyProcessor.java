package com.raynigon.ecs.logging.access.processor;

import ch.qos.logback.access.spi.IAccessEvent;
import com.raynigon.ecs.logging.access.event.EcsAccessLogEvent;

public class BodyProcessor implements AccessEventProcessor {
    @Override
    public EcsAccessLogEvent process(EcsAccessLogEvent result, IAccessEvent event) {
        return result.toBuilder()
                .requestBody(event.getRequestContent())
                .responseBody(event.getResponseContent())
                .build();
    }
}
