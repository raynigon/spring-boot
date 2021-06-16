package com.raynigon.ecs.logging.processor;

import ch.qos.logback.core.spi.DeferredProcessingAware;
import com.raynigon.ecs.logging.event.EcsLogEvent;

public interface EventProcessor<R extends EcsLogEvent, E extends DeferredProcessingAware> {
    R process(R result, E event);
}
