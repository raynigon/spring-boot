package com.raynigon.ecs.logging.async.service.helper


import spock.lang.Specification

import java.util.concurrent.Callable
import java.util.function.Supplier

class NoOpTimerSpec extends Specification {

    def "supplier gets executed"() {
        given:
        NoOpTimer wrapper = new NoOpTimer();
        Supplier supplier = Mock()

        when:
        wrapper.record(supplier)

        then:
        1 * supplier.get() >> null
    }

    def "callable gets executed"() {
        given:
        NoOpTimer wrapper = new NoOpTimer();
        Callable callable = Mock()

        when:
        wrapper.recordCallable(callable)

        then:
        1 * callable.call() >> null
    }

    def "start created sample"() {
        expect:
        new NoOpTimer().start() instanceof NoOpSample
    }

    def "stop returns minus 1"() {
        expect:
        new NoOpTimer().stop(null) == -1
    }
}
