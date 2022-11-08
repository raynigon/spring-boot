package com.raynigon.ecs.logging.async.service

import spock.lang.Specification
import spock.lang.Subject

class NoOpMetricsServiceSpec extends Specification {

    @Subject
    NoOpMetricsService service = new NoOpMetricsService()

    def "create queue timer"() {
        expect:
        service.createQueueTimer(String.class) != null
    }

    def "create execution timer"() {
        expect:
        service.createExecutionTimer(String.class) != null
    }
}
