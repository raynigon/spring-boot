package com.raynigon.ecs.logging.application.processor;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.raynigon.ecs.logging.application.event.EcsApplicationLogEvent;

import java.util.HashMap;
import java.util.Map;

import static com.raynigon.ecs.logging.LoggingConstants.SESSION_ID_PROPERTY;
import static com.raynigon.ecs.logging.LoggingConstants.TRANSACTION_ID_PROPERTY;

public class MdcPropertyProcessor implements ApplicationEventProcessor {
    @Override
    public EcsApplicationLogEvent process(EcsApplicationLogEvent result, ILoggingEvent event) {
        Map<String, String> mdcPropertyMap = event.getMDCPropertyMap();
        if (mdcPropertyMap == null || mdcPropertyMap.isEmpty()) return result;
        Map<String, String> labels = new HashMap<>(mdcPropertyMap);

        String transactionId = extractProperty(labels, TRANSACTION_ID_PROPERTY);
        String sessionId = extractProperty(labels, SESSION_ID_PROPERTY);

        return result.toBuilder()
                .labels(labels)
                .transactionId(transactionId)
                .sessionId(sessionId)
                .build();
    }

    private String extractProperty(Map<String, String> labels, String property) {
        String transactionId = null;
        if (labels.containsKey(property)) {
            transactionId = labels.get(property);
            labels.remove(property);
        }
        return transactionId;
    }
}
