package com.raynigon.ecs.logging.async.executor;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.atomic.AtomicReference;

import static com.raynigon.ecs.logging.async.executor.MdcConcurrentExecutionHelper.afterExecution;
import static com.raynigon.ecs.logging.async.executor.MdcConcurrentExecutionHelper.beforeExecution;

@RequiredArgsConstructor
public class MdcForkJoinTask<T> extends ForkJoinTask<T> {

    private static final long serialVersionUID = 1L;

    private final ForkJoinTask<T> task;

    private final Map<String, String> newContext;

    /**
     * If non-null, overrides the value returned by the underlying task.
     */
    private final AtomicReference<T> override = new AtomicReference<>();

    @Override
    public T getRawResult()
    {
        T result = override.get();
        if (result != null)
            return result;
        return task.getRawResult();
    }

    @Override
    protected void setRawResult(T value)
    {
        override.set(value);
    }

    @Override
    protected boolean exec()
    {
        // According to ForkJoinTask.fork() "it is a usage error to fork a task more than once unless it has completed
        // and been reinitialized". We therefore assume that this method does not have to be thread-safe.
        Map<String, String> oldContext = beforeExecution(newContext);
        try
        {
            task.invoke();
            return true;
        }
        finally
        {
            afterExecution(oldContext);
        }
    }
}
