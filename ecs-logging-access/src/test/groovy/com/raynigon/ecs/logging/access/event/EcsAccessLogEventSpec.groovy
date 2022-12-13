package com.raynigon.ecs.logging.access.event

import spock.lang.Specification

import java.time.Duration

class EcsAccessLogEventSpec extends Specification {

    @SuppressWarnings('ChangeToOperator')
    def "Two equal EcsAccessLogEvent objects are equal"() {
        given: "Two equal EcsAccessLogEvent objects exist"
        // First Object
        EcsAccessLogEvent object0 = EcsAccessLogEvent.builder()
                .build()
        // Second Object
        EcsAccessLogEvent object1 = EcsAccessLogEvent.builder()
                .build()

        when: "The two objects are compared"
        boolean result = object0.equals(object1)


        then: "The comparison result is true"
        result
    }

    @SuppressWarnings('ChangeToOperator')
    def "Two different EcsAccessLogEvent objects are equal"() {
        given: "Two equal EcsAccessLogEvent objects exist"
        // First Object
        EcsAccessLogEvent object0 = EcsAccessLogEvent.builder()
                .serviceName("alice")
                .build()

        // Second Object
        EcsAccessLogEvent object1 = EcsAccessLogEvent.builder()
                .serviceName("bob")
                .build()

        when: "The two objects are compared"
        boolean result = object0.equals(object1)


        then: "The comparison result is true"
        !result
    }

    def "Two equal EcsAccessLogEvent objects have the same hashCode"() {
        given: "Two equal EcsAccessLogEvent objects exist"
        // First Object
        EcsAccessLogEvent object0 = EcsAccessLogEvent.builder()
                .build()

        // Second Object
        EcsAccessLogEvent object1 = EcsAccessLogEvent.builder()
                .build()

        when: "The hashCode is generated"
        int hc0 = object0.hashCode()
        int hc1 = object1.hashCode()


        then: "The hashCodes are equal"
        hc0 == hc1
    }

    def "Two different EcsAccessLogEvent objects have different hashCodes"() {
        given: "Two equal EcsAccessLogEvent objects exist"
        // First Object
        EcsAccessLogEvent object0 = EcsAccessLogEvent.builder()
                .serviceName("alice")
                .build()

        // Second Object
        EcsAccessLogEvent object1 = EcsAccessLogEvent.builder()
                .serviceName("bob")
                .build()

        when: "The hashCode is generated"
        int hc0 = object0.hashCode()
        int hc1 = object1.hashCode()


        then: "The hashCodes are equal"
        hc0 != hc1
    }

    def "ToString works correctly"() {
        given: "EcsAccessLogEvent object gets created"
        EcsAccessLogEvent object = EcsAccessLogEvent.builder()
                .build()

        when: "ToString is called"
        String result = object.toString()

        then: "The hashCodes are equal"
        result.contains("EcsAccessLogEvent")
    }

    def "GetDuration works correctly"() {
        given: "EcsAccessLogEvent object gets created"
        EcsAccessLogEvent object = EcsAccessLogEvent.builder()
                .duration(Duration.ofMillis(1))
                .build()

        when: "ToString is called"
        Long duration = object.getDuration()

        then: "The hashCodes are equal"
        duration == 1
    }

    def "GetDuration returns null if no duration exists"() {
        given: "EcsAccessLogEvent object gets created"
        EcsAccessLogEvent object = EcsAccessLogEvent.builder()
                .duration(null)
                .build()

        when: "ToString is called"
        Long duration = object.getDuration()

        then: "The hashCodes are equal"
        duration == null
    }
}
