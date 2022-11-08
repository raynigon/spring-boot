package com.raynigon.ecs.logging.async.service;

import com.raynigon.ecs.logging.async.service.helper.MicrometerTimer;
import com.raynigon.ecs.logging.async.service.helper.TimerWrapper;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ConditionalOnClass(MeterRegistry.class)
@ConditionalOnProperty(name = "raynigon.logging.async.metrics.enabled", havingValue = "true")
public class MicrometerMetricsService implements AsyncMetricsService {


    private static final String QUEUE_TIMER_NAME = "raynigon.async.service.queue.duration";
    private static final String EXECUTION_TIMER_NAME = "raynigon.async.service.execution.duration";

    private final MeterRegistry meterRegistry;

    @Override
    public TimerWrapper createQueueTimer(Class<?> source) {
        Timer timer = meterRegistry.timer(QUEUE_TIMER_NAME, "source", formatSource(source));
        return wrap(timer);
    }

    @Override
    public TimerWrapper createExecutionTimer(Class<?> source) {
        Timer timer = meterRegistry.timer(EXECUTION_TIMER_NAME, "source", formatSource(source));
        return wrap(timer);
    }

    private TimerWrapper wrap(Timer timer) {
        return new MicrometerTimer(timer);
    }

    private String formatSource(Class<?> source) {
        return source.getName().split("/")[0];
    }
}
