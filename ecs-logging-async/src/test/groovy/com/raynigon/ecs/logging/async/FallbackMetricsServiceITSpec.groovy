package com.raynigon.ecs.logging.async

import com.raynigon.ecs.logging.async.helper.MeterRegistryProvider
import com.raynigon.ecs.logging.async.service.AsyncMetricsService
import com.raynigon.ecs.logging.async.service.NoOpMetricsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@EnableAutoConfiguration
@ContextConfiguration(classes = [MdcExecutorConfiguration, MeterRegistryProvider])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = [])
class FallbackMetricsServiceITSpec extends Specification {

    @Autowired
    AsyncMetricsService metricsService

    def "test"(){
        expect:
        metricsService instanceof  NoOpMetricsService
    }
}
