package com.raynigon.ecs.logging.application.processor;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.raynigon.ecs.logging.application.event.EcsApplicationLogEvent;
import com.raynigon.ecs.logging.processor.EventProcessor;

public interface ApplicationEventProcessor extends EventProcessor<EcsApplicationLogEvent, ILoggingEvent> {
}
