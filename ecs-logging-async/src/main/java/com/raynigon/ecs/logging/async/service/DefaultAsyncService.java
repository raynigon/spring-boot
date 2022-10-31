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

    private static final String TIMER_NAME = "raynigon.async.service.duration";

    private final MdcForkJoinPool forkJoinPool;

    private final MeterRegistry meterRegistry;

    private static final Logger log = LoggerFactory.getLogger(DefaultAsyncService.class);

    @Override
    public <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {
        String sourceName = supplier.getClass().getName();
        Timer timer = meterRegistry.timer(TIMER_NAME, "source", sourceName);
        Timer.Sample sample = Timer.start();
        Supplier<U> wrapped = () -> {
            U result;
            try {
                result = supplier.get();
            } finally {
                long nanoseconds = sample.stop(timer);
                log.trace("The supplier {} took {} ms", sourceName, nanoseconds / 1000000.0);
            }
            return result;
        };
        log.trace("Add supplier {} to ForkJoinPool", supplier);
        return CompletableFuture.supplyAsync(wrapped, forkJoinPool);
    }

    @Override
    public <V> ForkJoinTask<V> submit(Callable<V> callable) {
        String sourceName = callable.getClass().getName();
        Timer timer = meterRegistry.timer(TIMER_NAME, "source", sourceName);
        Timer.Sample sample = Timer.start();
        Callable<V> wrapped = () -> {
            V result;
            try {
                result = callable.call();
            } finally {
                long nanoseconds = sample.stop(timer);
                log.trace("The callable {} took {} ms", sourceName, nanoseconds / 1000000.0);
            }
            return result;
        };
        return forkJoinPool.submit(wrapped);
    }
}
