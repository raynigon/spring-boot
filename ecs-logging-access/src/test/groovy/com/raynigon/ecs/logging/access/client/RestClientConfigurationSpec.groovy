package com.raynigon.ecs.logging.access.client

import org.slf4j.MDC
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.client.ClientHttpRequest
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.DefaultRestClient
import org.springframework.web.client.RestClient
import spock.lang.Specification

import static com.raynigon.ecs.logging.LoggingConstants.TRANSACTION_ID_HEADER
import static com.raynigon.ecs.logging.LoggingConstants.TRANSACTION_ID_PROPERTY

class RestClientConfigurationSpec extends Specification {

    def 'interceptor is added to rest client builder'() {
        given:
        RestClient.Builder builder = Mock()

        and:
        RestClientConfiguration configuration = new RestClientConfiguration()

        when:
        configuration.customize(builder)

        then:
        1 * builder.requestInterceptor(_)
    }


    @SuppressWarnings('GroovyAccessibility')
    def 'interceptor is added to rest client derived from builder'() {
        given:
        RestClient.Builder builder = RestClient.builder()

        and:
        RestClientConfiguration configuration = new RestClientConfiguration()

        when:
        configuration.customize(builder)

        and: "A RestClient is created from the builder"
        RestClient client = builder.build()

        and: "Check the field in the DefaultRestClient"
        def field = DefaultRestClient.class.getDeclaredField("interceptors")
        field.setAccessible(true)

        then:
        field.get(client).size() == 1
    }

    def 'interceptor adds transaction id'() {
        given: "A ClientHttpRequestFactory"
        ClientHttpRequestFactory requestFactory = Mock()
        ClientHttpRequest request = Mock()
        requestFactory.createRequest(_, _) >> request

        and: "A HttpRequest with headers"
        HttpHeaders headers = new HttpHeaders()
        request.getHeaders() >> headers
        request.getAttributes() >> [:]

        and: "A HttpResponse"
        ClientHttpResponse response = Mock()
        request.execute() >> response
        response.statusCode >> HttpStatus.OK

        and: "A RestClient Builder"
        RestClient.Builder builder = RestClient.builder()
                .requestFactory(requestFactory)

        and: "A RestClientConfiguration"
        RestClientConfiguration configuration = new RestClientConfiguration()

        and: "A transaction id in the MDC"
        MDC.clear()
        MDC.put(TRANSACTION_ID_PROPERTY, "test-123")

        when: "The configuration is applied to the builder"
        configuration.customize(builder)

        and: "A RestClient is created from the builder"
        RestClient client = builder.build()

        and: "A request is executed"
        client.get().uri("https://google.com").retrieve().toBodilessEntity()

        then: "The transaction id is added to the request"
        headers.containsKey(TRANSACTION_ID_HEADER)
        headers.get(TRANSACTION_ID_HEADER).first() == "test-123"
    }
}