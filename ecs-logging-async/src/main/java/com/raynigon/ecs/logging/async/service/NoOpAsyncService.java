package com.raynigon.ecs.logging.async.service;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.function.Supplier;

/**
 * This should only be used in tests, because it does not manage MDC Tags etc.
 * but simply executes all given tasks.
 */
public class NoOpAsyncService implements AsyncService {
    @Override
    public <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {
        return CompletableFuture.supplyAsync(supplier);
    }

    @Override
    public <V> ForkJoinTask<V> submit(Callable<V> callable) {
        return ForkJoinPool.commonPool().submit(callable);
    }
}
