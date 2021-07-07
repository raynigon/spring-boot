package com.raynigon.ecs.logging.application.converter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.raynigon.ecs.logging.application.event.EcsApplicationLogEvent;
import com.raynigon.ecs.logging.application.processor.ApplicationEventProcessor;
import com.raynigon.ecs.logging.application.processor.MdcPropertyProcessor;
import com.raynigon.ecs.logging.application.processor.ServiceNameProcessor;
import com.raynigon.ecs.logging.application.processor.ThrowableProcessor;
import com.raynigon.ecs.logging.converter.EventConverter;
import com.raynigon.ecs.logging.converter.EventConverterHelper;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class EcsApplicationConverter implements EventConverter<ILoggingEvent, EcsApplicationLogEvent> {

    private final List<ApplicationEventProcessor> processors;

    public EcsApplicationConverter() {
        processors = List.of(
                new MdcPropertyProcessor(),
                new ServiceNameProcessor(),
                new ThrowableProcessor()
        );
    }

    @Override
    public EcsApplicationLogEvent convert(ILoggingEvent event) {
        EcsApplicationLogEvent result = EcsApplicationLogEvent.builder()
                .timestamp(OffsetDateTime.ofInstant(Instant.ofEpochMilli(event.getTimeStamp()), ZoneOffset.UTC))
                .serviceName("spring-application")
                .message(event.getFormattedMessage() != null ? event.getFormattedMessage().trim() : "")
                .level(event.getLevel() != null ? event.getLevel().levelStr.toUpperCase() : null)
                .logger(event.getLoggerName())
                .threadName(event.getThreadName())
                .build();
        return EventConverterHelper.apply(processors, result, event);
    }
}
