package com.raynigon.ecs.logging.async.service;

import com.raynigon.ecs.logging.async.executor.MdcForkJoinPool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinTask;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class DefaultAsyncService implements AsyncService {

    private final MdcForkJoinPool forkJoinPool;

    @Override
    public <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {
        return CompletableFuture.supplyAsync(supplier, forkJoinPool);
    }

    @Override
    public <V> ForkJoinTask<V> submit(Callable<V> callable) {
        return forkJoinPool.submit(callable);
    }
}
