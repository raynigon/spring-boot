package com.raynigon.ecs.logging.async.service;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinTask;
import java.util.function.Supplier;

public interface AsyncService {

    <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier);

    <V> ForkJoinTask<V> submit(Callable<V> callable);
}
