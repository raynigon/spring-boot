package com.raynigon.ecs.logging.async.scheduler;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;

import static com.raynigon.ecs.logging.LoggingConstants.TRANSACTION_ID_PROPERTY;
import static com.raynigon.ecs.logging.async.executor.MdcConcurrentExecutionHelper.afterExecution;
import static com.raynigon.ecs.logging.async.executor.MdcConcurrentExecutionHelper.beforeExecution;

@RequiredArgsConstructor
public class MdcScheduledRunnable implements Runnable {

    private final Logger log = LoggerFactory.getLogger(MdcScheduledRunnable.class);

    private final Runnable runnable;

    @Override
    public void run() {
        Map<String, String> mdcTags = Map.of(
                TRANSACTION_ID_PROPERTY, UUID.randomUUID().toString()
        );
        Map<String, String> previous = beforeExecution(mdcTags);
        try {
            runnable.run();
        } catch (Throwable t) {
            log.error("Unexpected " + t.getClass().getSimpleName() + " in Scheduled Task", t);
            throw new MdcScheduledRunnableException(mdcTags, t);
        } finally {
            afterExecution(previous);
        }
    }
}
