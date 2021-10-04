package com.raynigon.ecs.logging.access.converter;

import ch.qos.logback.access.spi.IAccessEvent;
import com.raynigon.ecs.logging.access.event.EcsAccessLogEvent;
import com.raynigon.ecs.logging.access.processor.*;
import com.raynigon.ecs.logging.converter.EventConverter;
import com.raynigon.ecs.logging.converter.EventConverterHelper;
import org.springframework.http.HttpHeaders;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class EcsAccessConverter implements EventConverter<IAccessEvent, EcsAccessLogEvent> {

    private final List<AccessEventProcessor> processors;

    public EcsAccessConverter() {
        processors = List.of(
                new DurationProcessor(),
                new ResponseSizeProcessor(),
                new ServiceNameProcessor(),
                new SessionIdProcessor(),
                new SourceAddressProcessor(),
                new TransactionIdProcessor()
        );
    }

    @Override
    public EcsAccessLogEvent convert(IAccessEvent event) {
        EcsAccessLogEvent result = EcsAccessLogEvent.builder()
                .timestamp(OffsetDateTime.ofInstant(Instant.ofEpochMilli(event.getTimeStamp()), ZoneOffset.UTC))
                .clientAddress(event.getRemoteAddr())
                .userName(event.getRemoteUser())
                .requestMethod(event.getMethod())
                .urlPath(event.getRequest().getRequestURI())
                .urlQuery(event.getQueryString())
                .responseStatus(event.getStatusCode())
                .userAgent(event.getRequestHeader(HttpHeaders.USER_AGENT))
                .build();
        return EventConverterHelper.apply(processors, result, event);
    }
}
