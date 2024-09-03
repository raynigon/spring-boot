package com.raynigon.ecs.logging.access.processor;

import ch.qos.logback.access.common.spi.IAccessEvent;
import com.raynigon.ecs.logging.access.event.EcsAccessLogEvent;

import static com.raynigon.ecs.logging.LoggingConstants.SESSION_ID_HEADER;

public class SessionIdProcessor implements AccessEventProcessor {
    @Override
    public EcsAccessLogEvent process(EcsAccessLogEvent result, IAccessEvent event) {
        String sessionId = event.getRequestHeader(SESSION_ID_HEADER);
        if (IAccessEvent.NA.equals(sessionId))
            return result;
        return result.toBuilder()
                .sessionId(sessionId)
                .build();
    }
}
