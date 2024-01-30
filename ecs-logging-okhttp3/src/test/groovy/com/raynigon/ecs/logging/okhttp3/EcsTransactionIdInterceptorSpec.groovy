package com.raynigon.ecs.logging.okhttp3

import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.slf4j.MDC;
import spock.lang.Specification
import spock.lang.Subject
import okhttp3.Interceptor

import static com.raynigon.ecs.logging.LoggingConstants.TRANSACTION_ID_HEADER
import static com.raynigon.ecs.logging.LoggingConstants.TRANSACTION_ID_PROPERTY

class EcsTransactionIdInterceptorSpec extends Specification {

    @Subject
    EcsTransactionIdInterceptor interceptor = new EcsTransactionIdInterceptor()

    def "transaction id header exists"() {
        given:
        Interceptor.Chain chain = Mock()
        Request request = new Request.Builder().url("https://example.com").build()
        Response response = new Response.Builder().request(request).code(200)
                .protocol(Protocol.HTTP_1_1)
                .message("dummy")
                .build()

        when:
        def result = interceptor.intercept(chain)

        then:
        1 * chain.request() >> request
        1 * chain.proceed({ Request r -> r.header(TRANSACTION_ID_HEADER) != null }) >> response

        and:
        result == response

        cleanup:
        MDC.clear()
    }

    def "transaction id header is set from MDC Tag"() {
        given:
        Interceptor.Chain chain = Mock()
        Request request = new Request.Builder().url("https://example.com").build()
        Response response = new Response.Builder().request(request).code(200)
                .protocol(Protocol.HTTP_1_1)
                .message("dummy")
                .build()

        and:
        MDC.put(TRANSACTION_ID_PROPERTY, "my-value")

        when:
        def result = interceptor.intercept(chain)

        then:
        1 * chain.request() >> request
        1 * chain.proceed({ Request r -> r.header(TRANSACTION_ID_HEADER) == "my-value" }) >> response

        and:
        result == response

        cleanup:
        MDC.clear()
    }
}