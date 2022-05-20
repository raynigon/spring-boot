package com.raynigon.ecs.logging.access.processor;

import ch.qos.logback.access.spi.IAccessEvent;
import com.raynigon.ecs.logging.access.event.EcsAccessLogEvent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BodyProcessor implements AccessEventProcessor {

    private final int bodySizeLimit;

    @Override
    public EcsAccessLogEvent process(EcsAccessLogEvent result, IAccessEvent event) {
        String requestBody = event.getRequestContent();
        String responseBody = event.getResponseContent();
        requestBody = limitSize(requestBody);
        responseBody = limitSize(responseBody);

        return result.toBuilder()
                .requestBody(requestBody)
                .responseBody(responseBody)
                .build();
    }

    private String limitSize(String body) {
        if (body == null || body.isEmpty()) return body;
        if (body.length() <= bodySizeLimit) return body;
        return body.substring(0, bodySizeLimit);
    }
}
