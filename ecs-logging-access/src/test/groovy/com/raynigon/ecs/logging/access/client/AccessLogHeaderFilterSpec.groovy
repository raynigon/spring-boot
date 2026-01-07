package com.raynigon.ecs.logging.access.client

import static com.raynigon.ecs.logging.LoggingConstants.SESSION_ID_HEADER
import static com.raynigon.ecs.logging.LoggingConstants.SESSION_ID_PROPERTY
import static com.raynigon.ecs.logging.LoggingConstants.TRANSACTION_ID_HEADER
import static com.raynigon.ecs.logging.LoggingConstants.TRANSACTION_ID_PROPERTY

import org.slf4j.MDC
import org.springframework.http.HttpHeaders
import spock.lang.Specification
import spock.lang.Subject

class AccessLogHeaderFilterSpec extends Specification {

    @Subject
    AccessLogHeaderFilter filter = new AccessLogHeaderFilter()

    def 'transaction id gets generated on empty MDC Tags'() {
        given:
        MDC.clear()
        HttpHeaders headers = new HttpHeaders()

        when:
        filter.accept(headers)

        then:
        headers.containsHeader(TRANSACTION_ID_HEADER)
    }

    def 'transaction id gets copied to x-request-id header'() {
        given:
        MDC.clear()
        MDC.put(TRANSACTION_ID_PROPERTY, "12345")
        HttpHeaders headers = new HttpHeaders()

        when:
        filter.accept(headers)

        then:
        headers[TRANSACTION_ID_HEADER] == ["12345"]
    }

    def 'session id gets copied to x-session-id header'() {
        given:
        MDC.clear()
        MDC.put(SESSION_ID_PROPERTY, "12345")
        HttpHeaders headers = new HttpHeaders()

        when:
        filter.accept(headers)

        then:
        headers[SESSION_ID_HEADER] == ["12345"]
    }
}
