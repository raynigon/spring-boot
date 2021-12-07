package com.raynigon.ecs.logging.access.client

import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ExchangeFunction
import spock.lang.Specification

class AccessLogExchangeFilterFunctionSpec extends Specification {

    def 'filter chain is executed'() {
        given:
        AccessLogExchangeFilterFunction filter = new AccessLogExchangeFilterFunction()
        ClientRequest request = Mock()
        ExchangeFunction next = Mock()

        and:
        HttpHeaders headers = new HttpHeaders()
        request.headers() >> headers

        when:
        filter.filter(request, next)

        then:
        1 * next.exchange(_)
    }
}
