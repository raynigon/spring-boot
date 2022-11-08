package com.raynigon.ecs.logging.async.service.helper

import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import io.micrometer.core.instrument.Timer;
import spock.lang.Specification

class MicrometerSampleSpec extends Specification {

    def "can be created"() {
        given:
        Timer timer = new SimpleMeterRegistry().timer("test")
        Timer.Sample sample = Timer.start()

        when:
        def result = new MicrometerSample(timer, sample)

        then:
        noExceptionThrown()
    }

    def "can be stopped"() {
        given:
        Timer timer = new SimpleMeterRegistry().timer("test")
        Timer.Sample sample = Timer.start()
        MicrometerSample wrapper = new MicrometerSample(timer, sample)

        when:
        def result = wrapper.stop()

        then:
        result > 0
    }
}
