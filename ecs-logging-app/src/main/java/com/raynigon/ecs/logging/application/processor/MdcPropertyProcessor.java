package com.raynigon.ecs.logging.application.processor;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.raynigon.ecs.logging.application.event.EcsApplicationLogEvent;

import java.util.HashMap;
import java.util.Map;

import static com.raynigon.ecs.logging.LoggingConstants.CORRELATION_ID_PROPERTY;

public class MdcPropertyProcessor implements ApplicationEventProcessor {
    @Override
    public EcsApplicationLogEvent process(EcsApplicationLogEvent result, ILoggingEvent event) {
        Map<String, String> mdcPropertyMap = event.getMDCPropertyMap();
        if (mdcPropertyMap == null || mdcPropertyMap.isEmpty()) return result;
        Map<String, String> labels = new HashMap<>(mdcPropertyMap);
        String traceId = null;
        if (labels.containsKey(CORRELATION_ID_PROPERTY)) {
            traceId = labels.get(CORRELATION_ID_PROPERTY);
            labels.remove(CORRELATION_ID_PROPERTY);
        }
        return result.toBuilder()
                .labels(labels)
                .traceId(traceId)
                .build();
    }
}
