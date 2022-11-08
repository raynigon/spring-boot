package com.raynigon.ecs.logging.async.service.helper

import spock.lang.Specification

class NoOpSampleSpec extends Specification {

    def "can be created"() {
        expect:
        new NoOpSample() != null
    }
}
