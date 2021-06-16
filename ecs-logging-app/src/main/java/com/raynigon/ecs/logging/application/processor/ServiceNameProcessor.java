package com.raynigon.ecs.logging.application.processor;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.raynigon.ecs.logging.application.event.EcsApplicationLogEvent;

import static com.raynigon.ecs.logging.LoggingConstants.SERVICE_NAME_PROPERTY;

public class ServiceNameProcessor implements ApplicationEventProcessor {
    @Override
    public EcsApplicationLogEvent process(EcsApplicationLogEvent result, ILoggingEvent event) {
        String serviceName = event.getLoggerContextVO().getPropertyMap().get(SERVICE_NAME_PROPERTY);
        if (serviceName == null || serviceName.isBlank()) return result;
        return result.toBuilder().serviceName(serviceName).build();
    }
}
