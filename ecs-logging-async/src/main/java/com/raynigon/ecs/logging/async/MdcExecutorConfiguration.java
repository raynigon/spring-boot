package com.raynigon.ecs.logging.async;

import com.raynigon.ecs.logging.async.config.AsyncLoggingConfiguration;
import com.raynigon.ecs.logging.async.executor.DefaultMdcForkJoinPool;
import com.raynigon.ecs.logging.async.model.MdcRunnable;
import com.raynigon.ecs.logging.async.scheduler.MdcScheduledExecutorService;
import com.raynigon.ecs.logging.async.service.AsyncMetricsService;
import com.raynigon.ecs.logging.async.service.MicrometerMetricsService;
import com.raynigon.ecs.logging.async.service.NoOpMetricsService;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("Async-");
        executor.setTaskDecorator(MdcRunnable::new);
        executor.initialize();
        return executor;
    }

    @Bean
    @ConditionalOnClass(MeterRegistry.class)
    @ConditionalOnProperty(name = "raynigon.logging.async.metrics.enabled", havingValue = "true")
    public AsyncMetricsService micrometerAsyncMetricsService(MeterRegistry meterRegistry){
        return new MicrometerMetricsService(meterRegistry);
    }

    @Bean
    @ConditionalOnMissingBean(AsyncMetricsService.class)
    public AsyncMetricsService fallbackAsyncMetricsService(){
        return new NoOpMetricsService();
    }
}
