package com.raynigon.ecs.logging.converter;

import ch.qos.logback.core.spi.DeferredProcessingAware;
import com.raynigon.ecs.logging.event.EcsLogEvent;
import com.raynigon.ecs.logging.processor.EventProcessor;

import java.util.List;

public class EventConverterHelper {

    public static <R extends EcsLogEvent, E extends DeferredProcessingAware, P extends EventProcessor<R, E>> R apply(List<P> processors, R result, E event) {
        R resultingEvent = result;
        for (EventProcessor<R, E> processor : processors) {
            resultingEvent = processor.process(resultingEvent, event);
        }
        return resultingEvent;
    }
}
