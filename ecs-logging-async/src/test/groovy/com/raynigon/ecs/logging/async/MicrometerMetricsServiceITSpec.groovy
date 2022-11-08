package com.raynigon.ecs.logging.async

import com.raynigon.ecs.logging.async.helper.MeterRegistryProvider
import com.raynigon.ecs.logging.async.service.AsyncMetricsService
import com.raynigon.ecs.logging.async.service.MicrometerMetricsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@EnableAutoConfiguration
@ContextConfiguration(classes = [MdcExecutorConfiguration, MeterRegistryProvider])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = ["raynigon.logging.async.metrics.enabled=true"])
class MicrometerMetricsServiceITSpec extends Specification {

    @Autowired
    AsyncMetricsService metricsService

    def "test"() {
        expect:
        metricsService instanceof MicrometerMetricsService
    }
}
