package com.raynigon.ecs.logging.async.executor;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.MDC;

import static com.raynigon.ecs.logging.async.executor.MdcConcurrentExecutionHelper.afterExecution;
import static com.raynigon.ecs.logging.async.executor.MdcConcurrentExecutionHelper.beforeExecution;

/**
 * A {@link ForkJoinPool} that inherits MDC contexts from the thread that queues a task.
 *
 * @author Gili Tzabari
 */
public final class MdcForkJoinPool extends ForkJoinPool {
    /**
     * Creates a new MdcForkJoinPool.
     *
     * @throws IllegalArgumentException if parallelism less than or equal to zero, or greater than implementation limit
     * @throws NullPointerException     if the factory is null
     * @throws SecurityException        if a security manager exists and the caller is not permitted to modify threads
     *                                  because it does not hold
     *                                  {@link java.lang.RuntimePermission}{@code ("modifyThread")}
     */
    public MdcForkJoinPool() {
        super();
    }

    /**
     * Creates a new MdcForkJoinPool.
     *
     * @param parallelism the parallelism level. For default value, use {@link java.lang.Runtime#availableProcessors}.
     * @throws IllegalArgumentException if parallelism less than or equal to zero, or greater than implementation limit
     * @throws NullPointerException     if the factory is null
     * @throws SecurityException        if a security manager exists and the caller is not permitted to modify threads
     *                                  because it does not hold
     *                                  {@link java.lang.RuntimePermission}{@code ("modifyThread")}
     */
    public MdcForkJoinPool(int parallelism) {
        super(parallelism);
    }

    /**
     * Creates a new MdcForkJoinPool.
     *
     * @param parallelism the parallelism level. For default value, use {@link java.lang.Runtime#availableProcessors}.
     * @param factory     the factory for creating new threads. For default value, use
     *                    {@link #defaultForkJoinWorkerThreadFactory}.
     * @param handler     the handler for internal worker threads that terminate due to unrecoverable errors encountered
     *                    while executing tasks. For default value, use {@code null}.
     * @param asyncMode   if true, establishes local first-in-first-out scheduling mode for forked tasks that are never
     *                    joined. This mode may be more appropriate than default locally stack-based mode in applications
     *                    in which worker threads only process event-style asynchronous tasks. For default value, use
     *                    {@code false}.
     * @throws IllegalArgumentException if parallelism less than or equal to zero, or greater than implementation limit
     * @throws NullPointerException     if the factory is null
     * @throws SecurityException        if a security manager exists and the caller is not permitted to modify threads
     *                                  because it does not hold
     *                                  {@link java.lang.RuntimePermission}{@code ("modifyThread")}
     */
    public MdcForkJoinPool(int parallelism, ForkJoinWorkerThreadFactory factory, UncaughtExceptionHandler handler,
                           boolean asyncMode) {
        super(parallelism, factory, handler, asyncMode);
    }

    @Override
    public void execute(ForkJoinTask<?> task) {
        // See http://stackoverflow.com/a/19329668/14731
        super.execute(wrap(task, MDC.getCopyOfContextMap()));
    }

    @Override
    public void execute(Runnable task) {
        // See http://stackoverflow.com/a/19329668/14731
        super.execute(wrap(task, MDC.getCopyOfContextMap()));
    }

    private <T> ForkJoinTask<T> wrap(ForkJoinTask<T> task, Map<String, String> newContext) {
        return new MdcForkJoinTask<T>(task, newContext);
    }

    private Runnable wrap(Runnable task, Map<String, String> newContext) {
        return () ->
        {
            Map<String, String> oldContext = beforeExecution(newContext);
            try {
                task.run();
            } finally {
                afterExecution(oldContext);
            }
        };
    }
}
