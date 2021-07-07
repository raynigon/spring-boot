package com.raynigon.ecs.logging.access.processor;

import ch.qos.logback.access.spi.IAccessEvent;
import com.raynigon.ecs.logging.access.event.EcsAccessLogEvent;
import org.apache.catalina.connector.Response;

public class ResponseSizeProcessor implements AccessEventProcessor {
    @Override
    public EcsAccessLogEvent process(EcsAccessLogEvent result, IAccessEvent event) {
        if (!(event.getResponse() instanceof Response)) return result;
        Response response = (Response) event.getResponse();
        return result.toBuilder()
                .responseSize(response.getBytesWritten(true))
                .build();
    }
}
