package com.raynigon.ecs.logging.access.processor;

import ch.qos.logback.access.spi.IAccessEvent;
import com.raynigon.ecs.logging.access.event.EcsAccessLogEvent;

import static com.raynigon.ecs.logging.LoggingConstants.CORRELATION_ID_HEADER;

public class CorrelationIdProcessor implements AccessEventProcessor {
    @Override
    public EcsAccessLogEvent process(EcsAccessLogEvent result, IAccessEvent event) {
        return result.toBuilder()
                .traceId(event.getResponseHeader(CORRELATION_ID_HEADER))
                .build();
    }
}
