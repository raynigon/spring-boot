package com.raynigon.ecs.logging.access.client;

import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;

import java.util.UUID;
import java.util.function.Consumer;

import static com.raynigon.ecs.logging.LoggingConstants.*;
import static com.raynigon.ecs.logging.LoggingConstants.SESSION_ID_HEADER;

public class AccessLogHeaderFilter implements Consumer<HttpHeaders> {
    @Override
    public void accept(HttpHeaders headers) {
        String correlationId = MDC.get(TRANSACTION_ID_PROPERTY);
        String sessionId = MDC.get(SESSION_ID_PROPERTY);
        if (correlationId == null) {
            correlationId = UUID.randomUUID().toString();
        }
        if (!headers.containsKey(TRANSACTION_ID_HEADER)) {
            headers.add(TRANSACTION_ID_HEADER, correlationId);
        }
        if (sessionId != null && !headers.containsKey(SESSION_ID_HEADER)) {
            headers.add(SESSION_ID_HEADER, sessionId);
        }
    }
}
