package com.raynigon.ecs.logging.access.processor;

import ch.qos.logback.access.spi.IAccessEvent;
import com.raynigon.ecs.logging.access.event.EcsAccessEvent;
import com.raynigon.ecs.logging.access.event.EcsAccessLogEvent;

import static com.raynigon.ecs.logging.LoggingConstants.SERVICE_NAME_PROPERTY;

public class ServiceNameProcessor implements AccessEventProcessor {

    @Override
    public EcsAccessLogEvent process(EcsAccessLogEvent result, IAccessEvent event) {
        if (!(event instanceof EcsAccessEvent)) return result;

        String name = ((EcsAccessEvent) event).getContext().getProperty(SERVICE_NAME_PROPERTY);
        if (name == null || name.isBlank()) return result;

        return result.toBuilder()
                .serviceName(name)
                .build();
    }
}
