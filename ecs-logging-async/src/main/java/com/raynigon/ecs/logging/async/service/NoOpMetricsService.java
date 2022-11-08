package com.raynigon.ecs.logging.async.service;

import com.raynigon.ecs.logging.async.service.helper.NoOpTimer;
import com.raynigon.ecs.logging.async.service.helper.TimerWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnMissingBean(AsyncMetricsService.class)
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
