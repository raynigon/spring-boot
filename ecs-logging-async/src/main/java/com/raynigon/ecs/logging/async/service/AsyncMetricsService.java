package com.raynigon.ecs.logging.async.service;

import com.raynigon.ecs.logging.async.service.helper.TimerWrapper;

public interface AsyncMetricsService {

    TimerWrapper createQueueTimer(Class<?> source);

    TimerWrapper createExecutionTimer(Class<?> source);
}

