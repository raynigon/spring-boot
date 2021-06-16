package com.raynigon.ecs.logging.access.converter;

import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.raynigon.ecs.logging.access.event.EcsAccessLogEvent;
import com.raynigon.ecs.logging.access.processor.AccessEventProcessor;
import com.raynigon.ecs.logging.converter.EventConverter;
import com.raynigon.ecs.logging.converter.EventConverterHelper;
import org.springframework.http.HttpHeaders;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class EcsAccessConverter implements EventConverter<IAccessEvent, EcsAccessLogEvent> {

    private final List<AccessEventProcessor> processors;

    public EcsAccessConverter() {
        processors = ServiceLoader.load(AccessEventProcessor.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .collect(Collectors.toList());
    }

    @Override
    public EcsAccessLogEvent convert(IAccessEvent event) {
        EcsAccessLogEvent result = EcsAccessLogEvent.builder()
                .timestamp(OffsetDateTime.ofInstant(Instant.ofEpochMilli(event.getTimeStamp()), ZoneOffset.UTC))
                .sourceAddress(event.getRemoteAddr())
                .userName(event.getRemoteUser())
                .requestMethod(event.getMethod())
                .urlPath(event.getRequest().getRequestURI())
                .urlQuery(event.getQueryString())
                .responseStatus(event.getStatusCode())
                .responseSize(event.getContentLength())
                .userAgent(event.getRequestHeader(HttpHeaders.USER_AGENT))
                .build();
        return EventConverterHelper.apply(processors, result, event);
    }
}
