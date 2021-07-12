package com.raynigon.ecs.logging.access.processor;

import ch.qos.logback.access.spi.IAccessEvent;
import com.raynigon.ecs.logging.access.event.EcsAccessLogEvent;

import static com.raynigon.ecs.logging.LoggingConstants.SESSION_ID_HEADER;

public class SessionIdProcessor implements AccessEventProcessor {
    @Override
    public EcsAccessLogEvent process(EcsAccessLogEvent result, IAccessEvent event) {
        return result.toBuilder()
                .sessionId(event.getResponseHeader(SESSION_ID_HEADER))
                .build();
    }
}
