package com.raynigon.ecs.logging.async.service.helper

import io.micrometer.core.instrument.Timer
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import spock.lang.Specification

import java.util.concurrent.Callable
import java.util.function.Supplier

class MicrometerTimerSpec extends Specification {

    def "supplier gets executed"() {
        given:
        Timer timer = new SimpleMeterRegistry().timer("test")
        MicrometerTimer wrapper = new MicrometerTimer(timer);
        Supplier supplier = Mock()

        when:
        wrapper.record(supplier)

        then:
        1 * supplier.get() >> null

        and:
        timer.count() == 1
    }

    def "callable gets executed"() {
        given:
        Timer timer = new SimpleMeterRegistry().timer("test")
        MicrometerTimer wrapper = new MicrometerTimer(timer);
        Callable callable = Mock()

        when:
        wrapper.recordCallable(callable)

        then:
        1 * callable.call() >> null

        and:
        timer.count() == 1
    }

    def "start created sample"() {
        given:
        Timer timer = new SimpleMeterRegistry().timer("test")
        MicrometerTimer wrapper = new MicrometerTimer(timer);

        when:
        def result = wrapper.start()

        then:
        result instanceof MicrometerSample
    }

    def "stop returns minus duration"() {
        given:
        Timer timer = new SimpleMeterRegistry().timer("test")
        MicrometerTimer wrapper = new MicrometerTimer(timer);
        SampleWrapper sample = wrapper.start()

        when:
        def result = wrapper.stop(sample)

        then:
        result > 0
    }
}
