package com.raynigon.ecs.logging.async.scheduler;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;

import static com.raynigon.ecs.logging.LoggingConstants.*;
import static com.raynigon.ecs.logging.async.executor.MdcConcurrentExecutionHelper.afterExecution;
import static com.raynigon.ecs.logging.async.executor.MdcConcurrentExecutionHelper.beforeExecution;

@RequiredArgsConstructor
public class MdcScheduledExecutorService implements TaskScheduler {

    private final Logger log = LoggerFactory.getLogger(MdcScheduledExecutorService.class);
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
        return () -> {
            Map<String, String> mdcTags = Map.of(
                    TRANSACTION_ID_PROPERTY, UUID.randomUUID().toString()
            );
            Map<String, String> previous = beforeExecution(mdcTags);
            try {
                runnable.run();
            } catch (Throwable t) {
                log.error("Unexpected " + t.getClass().getSimpleName() + " in Scheduled Task", t);
            } finally {
                afterExecution(previous);
            }
        };
    }
}
