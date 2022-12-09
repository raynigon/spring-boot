package com.raynigon.ecs.logging.async.scheduler;

import com.raynigon.ecs.logging.async.model.MdcRunnable;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

@RequiredArgsConstructor
public class MdcScheduledExecutorService implements TaskScheduler {

    private final TaskScheduler delegatedTaskScheduler;

    @Override
    public ScheduledFuture<?> schedule(@NonNull Runnable task, @NonNull Trigger trigger) {
        return delegatedTaskScheduler.schedule(wrap(task), trigger);
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable task, Instant startTime) {
        return delegatedTaskScheduler.schedule(wrap(task), startTime);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Instant startTime, Duration period) {
        return delegatedTaskScheduler.scheduleAtFixedRate(wrap(task), startTime, period);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Duration period) {
        return delegatedTaskScheduler.scheduleAtFixedRate(wrap(task), period);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, Instant startTime, Duration delay) {
        return delegatedTaskScheduler.scheduleWithFixedDelay(wrap(task), startTime, delay);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, Duration delay) {
        return delegatedTaskScheduler.scheduleWithFixedDelay(wrap(task), delay);
    }

    private Runnable wrap(Runnable runnable) {
        return new MdcRunnable(runnable);
    }
}
