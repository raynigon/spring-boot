package com.raynigon.ecs.logging.access

import spock.lang.Specification

class AccessLogPropertiesSpec extends Specification {

    @SuppressWarnings('ChangeToOperator')
    def "Two equal AccessLogProperties objects are equal"() {
        given: "Two equal AccessLogProperties objects exist"
        // First Object
        AccessLogProperties properties0 = new AccessLogProperties()
        properties0.setExportBody(true)
        // Second Object
        AccessLogProperties properties1 = new AccessLogProperties()
        properties1.setExportBody(true)

        when: "The two objects are compared"
        boolean result = properties0.equals(properties1)


        then: "The comparison result is true"
        result
    }

    @SuppressWarnings('ChangeToOperator')
    def "Two different AccessLogProperties objects are equal"() {
        given: "Two equal AccessLogProperties objects exist"
        // First Object
        AccessLogProperties properties0 = new AccessLogProperties()
        properties0.setExportBody(true)
        // Second Object
        AccessLogProperties properties1 = new AccessLogProperties()
        properties1.setExportBody(false)

        when: "The two objects are compared"
        boolean result = properties0.equals(properties1)


        then: "The comparison result is true"
        !result
    }

    def "Two equal AccessLogProperties objects have the same hashCode"() {
        given: "Two equal AccessLogProperties objects exist"
        // First Object
        AccessLogProperties properties0 = new AccessLogProperties()
        properties0.setExportBody(true)
        // Second Object
        AccessLogProperties properties1 = new AccessLogProperties()
        properties1.setExportBody(true)

        when: "The hashCode is generated"
        int hc0 = properties0.hashCode()
        int hc1 = properties1.hashCode()


        then: "The hashCodes are equal"
        hc0 == hc1
    }

    def "Two different AccessLogProperties objects have different hashCodes"() {
        given: "Two equal AccessLogProperties objects exist"
        // First Object
        AccessLogProperties properties0 = new AccessLogProperties()
        properties0.setExportBody(true)
        // Second Object
        AccessLogProperties properties1 = new AccessLogProperties()
        properties1.setExportBody(false)

        when: "The hashCode is generated"
        int hc0 = properties0.hashCode()
        int hc1 = properties1.hashCode()


        then: "The hashCodes are equal"
        hc0 != hc1
    }

    def "ToString works correctly"() {
        given: "AccessLogProperties object gets created"
        AccessLogProperties properties = new AccessLogProperties()

        when: "ToString is called"
        String result = properties.toString()

        then: "The hashCodes are equal"
        result == "AccessLogProperties(excludeEndpoints=[], exportBody=false, bodySizeLimit=10000)"
    }

    def "Exclude Endpoints setter works correctly"() {
        given: "AccessLogProperties object gets created"
        AccessLogProperties properties = new AccessLogProperties()

        when: "The setter is called"
        properties.setExcludeEndpoints(["abc/123"])


        then: "The hashCodes are equal"
        properties.getExcludeEndpoints().size() == 1
        properties.getExcludeEndpoints().get(0) == "abc/123"
    }

    def "Body size limit setter works correctly"() {
        given: "AccessLogProperties object gets created"
        AccessLogProperties properties = new AccessLogProperties()

        when: "The setter is called"
        properties.setBodySizeLimit(123)

        then: "The hashCodes are equal"
        properties.getBodySizeLimit() == 123
    }

    def "Body size limit setter works correctly"() {
        given: "AccessLogProperties object gets created"
        AccessLogProperties properties = new AccessLogProperties()

        when: "The setter is called"
        properties.setExportBody(true)

        then: "The hashCodes are equal"
        properties.isExportBody()
    }
}
