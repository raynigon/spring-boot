package com.raynigon.ecs.logging.access.client

import org.springframework.http.HttpHeaders

import static com.raynigon.ecs.logging.LoggingConstants.TRANSACTION_ID_HEADER
import static com.raynigon.ecs.logging.LoggingConstants.TRANSACTION_ID_PROPERTY

import org.slf4j.MDC
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

class RestTemplateConfigurationSpec extends Specification {

    def 'interceptor is added to rest template'() {
        given:
        RestTemplate template0 = Mock()
        List<RestTemplate> templates = [template0]

        and:
        def interceptors0 = []
        template0.getInterceptors() >> interceptors0

        and:
        RestTemplateConfiguration configuration = new RestTemplateConfiguration(templates)

        when:
        configuration.configure()

        then:
        !interceptors0.empty
    }

    def 'interceptor adds transaction id'() {
        given:
        MDC.clear()
        MDC.put(TRANSACTION_ID_PROPERTY, "test-123")
        RestTemplate template0 = Mock()
        List<RestTemplate> templates = [template0]

        and:
        List<ClientHttpRequestInterceptor> interceptors0 = []
        template0.getInterceptors() >> interceptors0

        and:
        RestTemplateConfiguration configuration = new RestTemplateConfiguration(templates)
        configuration.configure()

        and:
        HttpRequest request = Mock()
        HttpHeaders headers = new HttpHeaders()
        byte[] body = new byte[0]
        ClientHttpRequestExecution execution = Mock()

        when:
        interceptors0[0].intercept(request, body, execution)

        then:
        1 * request.getHeaders() >> headers
        1 * execution.execute(_, _)

        and:
        headers.get(TRANSACTION_ID_HEADER) == ["test-123"]
    }
}
