package com.raynigon.ecs.logging.async.service;

import com.raynigon.ecs.logging.async.service.helper.NoOpTimer;
import com.raynigon.ecs.logging.async.service.helper.TimerWrapper;
import org.springframework.stereotype.Service;

public class NoOpMetricsService implements AsyncMetricsService {
    @Override
    public TimerWrapper createQueueTimer(Class<?> source) {
        return new NoOpTimer();
    }

    @Override
    public TimerWrapper createExecutionTimer(Class<?> source) {
        return new NoOpTimer();
    }
}
