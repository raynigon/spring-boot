package com.raynigon.ecs.logging.access.processor;

import ch.qos.logback.access.common.spi.IAccessEvent;
import com.raynigon.ecs.logging.access.event.EcsAccessLogEvent;

public class SourceAddressProcessor implements AccessEventProcessor {
    @Override
    public EcsAccessLogEvent process(EcsAccessLogEvent result, IAccessEvent event) {
        String xForwardForHeader = event.getRequestHeader("X-Forwarded-For");
        String sourceAddress = xForwardForHeader != null ? xForwardForHeader : event.getRemoteAddr();
        return result.toBuilder().clientAddress(sourceAddress).build();
    }
}
