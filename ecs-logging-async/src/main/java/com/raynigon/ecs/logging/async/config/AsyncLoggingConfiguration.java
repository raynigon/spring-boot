package com.raynigon.ecs.logging.async.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "raynigon.logging.async")
public class AsyncLoggingConfiguration {

    private int poolExecutors = Runtime.getRuntime().availableProcessors();
    private int taskExecutors = 1;
}
