package com.raynigon.ecs.logging.async.scheduler;

import com.raynigon.ecs.logging.async.model.MdcRunnable;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

@RequiredArgsConstructor
public class MdcScheduledExecutorService implements TaskScheduler {

    private final TaskScheduler delegatedTaskScheduler;

    @Override
    public ScheduledFuture<?> schedule(@NonNull Runnable task, @NonNull Trigger trigger) {
        return delegatedTaskScheduler.schedule(wrap(task), trigger);
    }

    @NonNull
    @Override
    public ScheduledFuture<?> schedule(@NonNull Runnable task, @NonNull Date startTime) {
        return delegatedTaskScheduler.schedule(wrap(task), startTime);
    }

    @NonNull
    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(@NonNull Runnable task, @NonNull Date startTime, long period) {
        return delegatedTaskScheduler.scheduleAtFixedRate(wrap(task), startTime, period);
    }

    @NonNull
    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(@NonNull Runnable task, long period) {
        return delegatedTaskScheduler.scheduleAtFixedRate(wrap(task), period);
    }

    @NonNull
    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(@NonNull Runnable task, @NonNull Date startTime, long delay) {
        return delegatedTaskScheduler.scheduleWithFixedDelay(wrap(task), startTime, delay);
    }

    @NonNull
    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(@NonNull Runnable task, long delay) {
        return delegatedTaskScheduler.scheduleWithFixedDelay(wrap(task), delay);
    }

    private Runnable wrap(Runnable runnable) {
        return new MdcRunnable(runnable);
    }
}
