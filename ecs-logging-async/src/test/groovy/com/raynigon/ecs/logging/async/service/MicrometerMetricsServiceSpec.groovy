package com.raynigon.ecs.logging.async.service

import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import spock.lang.Specification
import spock.lang.Subject

class MicrometerMetricsServiceSpec extends Specification {

    SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry()

    @Subject
    MicrometerMetricsService service = new MicrometerMetricsService(meterRegistry)

    def "create queue timer"() {
        when:
        service.createQueueTimer(String.class).record({ null })

        then:
        meterRegistry.find(MicrometerMetricsService.QUEUE_TIMER_NAME).timer().count() == 1
        meterRegistry.find(MicrometerMetricsService.EXECUTION_TIMER_NAME).timer() == null
    }

    def "create execution timer"() {
        when:
        service.createExecutionTimer(String.class).record({ null })

        then:
        meterRegistry.find(MicrometerMetricsService.QUEUE_TIMER_NAME).timer() == null
        meterRegistry.find(MicrometerMetricsService.EXECUTION_TIMER_NAME).timer().count() == 1
    }
}
