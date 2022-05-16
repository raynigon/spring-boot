package com.raynigon.ecs.logging.async;

import com.raynigon.ecs.logging.async.config.AsyncLoggingConfiguration;
import com.raynigon.ecs.logging.async.executor.DefaultMdcForkJoinPool;
import com.raynigon.ecs.logging.async.scheduler.MdcScheduledExecutorService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@EnableConfigurationProperties(AsyncLoggingConfiguration.class)
public class MdcExecutorConfiguration {

    private final DefaultMdcForkJoinPool globalPool;
    private final ConcurrentTaskScheduler taskScheduler;

    public MdcExecutorConfiguration(AsyncLoggingConfiguration config) {
        this.globalPool = new DefaultMdcForkJoinPool(config.getPoolExecutors());
        ScheduledExecutorService taskThreadPool = Executors.newSingleThreadScheduledExecutor();
        if (config.getTaskExecutors() > 1) {
            taskThreadPool = Executors.newScheduledThreadPool(config.getTaskExecutors());
        }
        this.taskScheduler = new ConcurrentTaskScheduler(taskThreadPool);
    }

    @Bean
    public DefaultMdcForkJoinPool mdcForkJoinPool() {
        return globalPool;
    }

    @Bean
    public TaskScheduler mdcTaskScheduler() {
        return new MdcScheduledExecutorService(taskScheduler);
    }
}
