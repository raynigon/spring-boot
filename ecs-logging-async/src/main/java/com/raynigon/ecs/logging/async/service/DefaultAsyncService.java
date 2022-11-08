package com.raynigon.ecs.logging.async.service;

import com.raynigon.ecs.logging.async.executor.MdcForkJoinPool;
import com.raynigon.ecs.logging.async.service.helper.SampleWrapper;
import com.raynigon.ecs.logging.async.service.helper.TimerWrapper;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinTask;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class DefaultAsyncService implements AsyncService {



    private final MdcForkJoinPool forkJoinPool;

    private final AsyncMetricsService metrics;

    private static final Logger log = LoggerFactory.getLogger(DefaultAsyncService.class);

    @Override
    public <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {
        TimerWrapper queueTimer = metrics.createQueueTimer(supplier.getClass());
        TimerWrapper execTimer = metrics.createExecutionTimer(supplier.getClass());
        SampleWrapper sample = queueTimer.start();
        Supplier<U> wrapped = () -> {
            long nanoseconds = queueTimer.stop(sample);
            log.trace("The supplier {} took {} ms in Queue", supplier.getClass(), nanoseconds / 1000000.0);
            return execTimer.record(supplier);
        };
        log.trace("Add supplier {} to ForkJoinPool", supplier);
        return CompletableFuture.supplyAsync(wrapped, forkJoinPool);
    }

    @Override
    public <V> ForkJoinTask<V> submit(Callable<V> callable) {
        TimerWrapper queueTimer = metrics.createQueueTimer(callable.getClass());
        TimerWrapper execTimer = metrics.createExecutionTimer(callable.getClass());
        SampleWrapper sample = queueTimer.start();
        Callable<V> wrapped = () -> {
            long nanoseconds = queueTimer.stop(sample);
            log.trace("The callable {} took {} ms in Queue", callable.getClass(), nanoseconds / 1000000.0);
            return execTimer.recordCallable(callable);
        };
        return forkJoinPool.submit(wrapped);
    }

}
