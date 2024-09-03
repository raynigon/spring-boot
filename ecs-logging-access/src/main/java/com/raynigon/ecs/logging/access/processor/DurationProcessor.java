package com.raynigon.ecs.logging.access.processor;

import ch.qos.logback.access.common.spi.IAccessEvent;
import com.raynigon.ecs.logging.access.event.EcsAccessEvent;
import com.raynigon.ecs.logging.access.event.EcsAccessLogEvent;

public class DurationProcessor implements AccessEventProcessor {
    @Override
    public EcsAccessLogEvent process(EcsAccessLogEvent result, IAccessEvent event) {
        if (!(event instanceof EcsAccessEvent)) return result;
        if (((EcsAccessEvent) event).getDuration().isZero()) return result;
        return result.toBuilder()
                .duration(((EcsAccessEvent) event).getDuration())
                .build();
    }
}
