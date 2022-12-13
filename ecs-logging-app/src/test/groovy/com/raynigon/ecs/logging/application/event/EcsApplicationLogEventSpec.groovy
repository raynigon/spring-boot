package com.raynigon.ecs.logging.application.event

import spock.lang.Specification

class EcsApplicationLogEventSpec extends Specification {

    @SuppressWarnings('ChangeToOperator')
    def "Two equal EcsApplicationLogEvent objects are equal"() {
        given: "Two equal EcsApplicationLogEvent objects exist"
        // First Object
        EcsApplicationLogEvent object0 = EcsApplicationLogEvent.builder()
                .build()
        // Second Object
        EcsApplicationLogEvent object1 = EcsApplicationLogEvent.builder()
                .build()

        when: "The two objects are compared"
        boolean result = object0.equals(object1)


        then: "The comparison result is true"
        result
    }

    @SuppressWarnings('ChangeToOperator')
    def "Two different EcsApplicationLogEvent objects are equal"() {
        given: "Two equal EcsApplicationLogEvent objects exist"
        // First Object
        EcsApplicationLogEvent object0 = EcsApplicationLogEvent.builder()
                .serviceName("alice")
                .build()

        // Second Object
        EcsApplicationLogEvent object1 = EcsApplicationLogEvent.builder()
                .serviceName("bob")
                .build()

        when: "The two objects are compared"
        boolean result = object0.equals(object1)


        then: "The comparison result is true"
        !result
    }

    def "Two equal EcsApplicationLogEvent objects have the same hashCode"() {
        given: "Two equal EcsApplicationLogEvent objects exist"
        // First Object
        EcsApplicationLogEvent object0 = EcsApplicationLogEvent.builder()
                .build()

        // Second Object
        EcsApplicationLogEvent object1 = EcsApplicationLogEvent.builder()
                .build()

        when: "The hashCode is generated"
        int hc0 = object0.hashCode()
        int hc1 = object1.hashCode()


        then: "The hashCodes are equal"
        hc0 == hc1
    }

    def "Two different EcsApplicationLogEvent objects have different hashCodes"() {
        given: "Two equal EcsApplicationLogEvent objects exist"
        // First Object
        EcsApplicationLogEvent object0 = EcsApplicationLogEvent.builder()
                .serviceName("alice")
                .build()

        // Second Object
        EcsApplicationLogEvent object1 = EcsApplicationLogEvent.builder()
                .serviceName("bob")
                .build()

        when: "The hashCode is generated"
        int hc0 = object0.hashCode()
        int hc1 = object1.hashCode()


        then: "The hashCodes are equal"
        hc0 != hc1
    }

    def "ToString works correctly"() {
        given: "EcsApplicationLogEvent object gets created"
        EcsApplicationLogEvent object = EcsApplicationLogEvent.builder()
                .build()

        when: "ToString is called"
        String result = object.toString()

        then: "The hashCodes are equal"
        result.contains("EcsApplicationLogEvent")
    }
}
