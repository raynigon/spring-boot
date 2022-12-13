package com.raynigon.ecs.logging.async.config

import spock.lang.Specification

class AsyncLoggingConfigurationSpec extends Specification {

    @SuppressWarnings('ChangeToOperator')
    def "Two equal AsyncLoggingConfiguration objects are equal"() {
        given: "Two equal AsyncLoggingConfiguration objects exist"
        // First Object
        AsyncLoggingConfiguration object0 = new AsyncLoggingConfiguration()
        // Second Object
        AsyncLoggingConfiguration object1 = new AsyncLoggingConfiguration()

        when: "The two objects are compared"
        boolean result = object0.equals(object1)


        then: "The comparison result is true"
        result
    }

    @SuppressWarnings('ChangeToOperator')
    def "Two different AsyncLoggingConfiguration objects are equal"() {
        given: "Two equal AsyncLoggingConfiguration objects exist"
        // First Object
        AsyncLoggingConfiguration object0 = new AsyncLoggingConfiguration()
        object0.setPoolExecutors(1)

        // Second Object
        AsyncLoggingConfiguration object1 = new AsyncLoggingConfiguration()
        object1.setPoolExecutors(2)

        when: "The two objects are compared"
        boolean result = object0.equals(object1)


        then: "The comparison result is true"
        !result
    }

    def "Two equal AsyncLoggingConfiguration objects have the same hashCode"() {
        given: "Two equal AsyncLoggingConfiguration objects exist"
        // First Object
        AsyncLoggingConfiguration object0 = new AsyncLoggingConfiguration()

        // Second Object
        AsyncLoggingConfiguration object1 = new AsyncLoggingConfiguration()

        when: "The hashCode is generated"
        int hc0 = object0.hashCode()
        int hc1 = object1.hashCode()


        then: "The hashCodes are equal"
        hc0 == hc1
    }

    def "Two different AsyncLoggingConfiguration objects have different hashCodes"() {
        given: "Two equal AsyncLoggingConfiguration objects exist"
        // First Object
        AsyncLoggingConfiguration object0 = new AsyncLoggingConfiguration()
        object0.setTaskExecutors(1)

        // Second Object
        AsyncLoggingConfiguration object1 = new AsyncLoggingConfiguration()
        object1.setTaskExecutors(2)

        when: "The hashCode is generated"
        int hc0 = object0.hashCode()
        int hc1 = object1.hashCode()


        then: "The hashCodes are equal"
        hc0 != hc1
    }

    def "ToString works correctly"() {
        given: "AsyncLoggingConfiguration object gets created"
        AsyncLoggingConfiguration object = new AsyncLoggingConfiguration()

        when: "ToString is called"
        String result = object.toString()

        then: "The hashCodes are equal"
        result.contains("AsyncLoggingConfiguration")
    }
}
