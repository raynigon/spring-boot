package com.raynigon.ecs.logging.async.service;

import com.raynigon.ecs.logging.async.executor.MdcForkJoinPool;
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

    private static final String QUEUE_TIMER_NAME = "raynigon.async.service.queue.duration";
    private static final String EXECUTION_TIMER_NAME = "raynigon.async.service.execution.duration";

    private final MdcForkJoinPool forkJoinPool;

    private final MeterRegistry meterRegistry;

    private static final Logger log = LoggerFactory.getLogger(DefaultAsyncService.class);

    @Override
    public <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {
        String sourceName = formatSource(supplier.getClass());
        Timer queueTimer = meterRegistry.timer(QUEUE_TIMER_NAME, "source", sourceName);
        Timer execTimer = meterRegistry.timer(EXECUTION_TIMER_NAME, "source", sourceName);
        Timer.Sample sample = Timer.start();
        Supplier<U> wrapped = () -> {
            long nanoseconds = sample.stop(queueTimer);
            log.trace("The supplier {} took {} ms in Queue", sourceName, nanoseconds / 1000000.0);
            return execTimer.record(supplier);
        };
        log.trace("Add supplier {} to ForkJoinPool", supplier);
        return CompletableFuture.supplyAsync(wrapped, forkJoinPool);
    }

    @Override
    public <V> ForkJoinTask<V> submit(Callable<V> callable) {
        String sourceName = formatSource(callable.getClass());
        Timer queueTimer = meterRegistry.timer(QUEUE_TIMER_NAME, "source", sourceName);
        Timer execTimer = meterRegistry.timer(EXECUTION_TIMER_NAME, "source", sourceName);
        Timer.Sample sample = Timer.start();
        Callable<V> wrapped = () -> {
            long nanoseconds = sample.stop(queueTimer);
            log.trace("The callable {} took {} ms in Queue", sourceName, nanoseconds / 1000000.0);
            return execTimer.recordCallable(callable);
        };
        return forkJoinPool.submit(wrapped);
    }

    private String formatSource(Class<?> source){
        return source.getName().split("/")[0];
    }
}
