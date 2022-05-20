package com.raynigon.ecs.logging.access.converter;

import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.core.Context;
import com.raynigon.ecs.logging.access.AccessLogProperties;
import com.raynigon.ecs.logging.access.context.IAccessLogContext;
import com.raynigon.ecs.logging.access.event.EcsAccessLogEvent;
import com.raynigon.ecs.logging.access.processor.*;
import com.raynigon.ecs.logging.converter.EventConverter;
import com.raynigon.ecs.logging.converter.EventConverterHelper;
import com.raynigon.ecs.logging.converter.LogbackConverter;
import org.springframework.http.HttpHeaders;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class EcsAccessConverter implements EventConverter<IAccessEvent, EcsAccessLogEvent>, LogbackConverter {

    private final List<AccessEventProcessor> processors;

    public EcsAccessConverter() {
        processors = new ArrayList<>(List.of(
                new DurationProcessor(),
                new ResponseSizeProcessor(),
                new ServiceNameProcessor(),
                new SessionIdProcessor(),
                new SourceAddressProcessor(),
                new TransactionIdProcessor()
        ));

    }

    @Override
    public void setup(Context context) {
        if (!(context instanceof IAccessLogContext)) {
            return;
        }
        AccessLogProperties properties = ((IAccessLogContext) context).getConfig();
        if (properties == null) {
            return;
        }
        if (properties.isExportBody()) {
            processors.add(new BodyProcessor(properties.getBodySizeLimit()));
        }
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
