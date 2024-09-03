package com.raynigon.ecs.logging.access.processor;

import ch.qos.logback.access.common.spi.IAccessEvent;
import com.raynigon.ecs.logging.access.event.EcsAccessLogEvent;
import com.raynigon.ecs.logging.processor.EventProcessor;

public interface AccessEventProcessor extends EventProcessor<EcsAccessLogEvent, IAccessEvent> {
}

