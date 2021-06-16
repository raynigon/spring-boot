package com.raynigon.ecs.logging.converter;

import com.raynigon.ecs.logging.event.EcsLogEvent;

public interface EventConverter<I, O extends EcsLogEvent> {

    O convert(I event);
}
