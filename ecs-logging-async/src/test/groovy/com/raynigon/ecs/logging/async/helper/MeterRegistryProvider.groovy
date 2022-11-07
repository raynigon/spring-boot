package com.raynigon.ecs.logging.async.helper

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MeterRegistryProvider {

    private final MeterRegistry meterRegistry = new SimpleMeterRegistry()

    @Bean
    MeterRegistry getMeterRegistry() {
        return meterRegistry
    }
}
