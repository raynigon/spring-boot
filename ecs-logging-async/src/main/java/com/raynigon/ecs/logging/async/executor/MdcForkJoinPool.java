package com.raynigon.ecs.logging.async.executor;

import org.springframework.lang.NonNull;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinTask;

public interface MdcForkJoinPool extends Executor, ExecutorService {

    void execute(ForkJoinTask<?> task);

    <T> ForkJoinTask<T> submit(ForkJoinTask<T> task);

    @Override
    @NonNull
    public <T> ForkJoinTask<T> submit(Callable<T> task);
    @Override
    @NonNull
    public <T> ForkJoinTask<T> submit(Runnable task, T result);

    @Override
    @NonNull
    public ForkJoinTask<?> submit(Runnable task);
}
