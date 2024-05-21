package com.raynigon.ecs.logging.async.service;

import com.raynigon.ecs.logging.async.service.helper.MicrometerTimer;
import com.raynigon.ecs.logging.async.service.helper.NoOpTimer;
import com.raynigon.ecs.logging.async.service.helper.TimerWrapper;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.binder.MeterBinder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

public class MicrometerMetricsService implements MeterBinder, AsyncMetricsService {


    private static final String QUEUE_TIMER_NAME = "raynigon.async.service.queue.duration";
    private static final String EXECUTION_TIMER_NAME = "raynigon.async.service.execution.duration";

    private MeterRegistry meterRegistry;

    @Override
    public void bindTo(@NonNull MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public TimerWrapper createQueueTimer(Class<?> source) {
        if (meterRegistry == null) {
            return new NoOpTimer();
        }
        Timer timer = meterRegistry.timer(QUEUE_TIMER_NAME, "source", formatSource(source));
        return wrap(timer);
    }

    @Override
    public TimerWrapper createExecutionTimer(Class<?> source) {
        if (meterRegistry == null) {
            return new NoOpTimer();
        }
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
