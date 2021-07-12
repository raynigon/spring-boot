package com.raynigon.ecs.logging.access

import com.raynigon.ecs.logging.access.server.TomcatAccessLogConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.test.context.ContextConfiguration
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import static com.raynigon.ecs.logging.LoggingConstants.*

@EnableAutoConfiguration
@ContextConfiguration(classes = TomcatAccessLogConfiguration)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = ["spring.application.name=my-test-app"])
class TomcatITSpec extends Specification {

    @LocalServerPort
    int port = 0

    def "http call without headers"() {
        given:
        RestTemplate restTemplate = new RestTemplate()

        when:
        restTemplate.getForEntity("http://localhost:$port/", Void)

        then:
        thrown(HttpClientErrorException.NotFound)
    }

    def "http call with given session id"() {
        given:
        RestTemplate restTemplate = new RestTemplate()
        MultiValueMap headers = new LinkedMultiValueMap()
        headers.add(SESSION_ID_HEADER, "test-123")
        RequestEntity requestEntity = new RequestEntity(null, headers, HttpMethod.GET, URI.create("http://localhost:$port/"))

        when:
        restTemplate.exchange(requestEntity, Void)

        then:
        thrown(HttpClientErrorException.NotFound)
    }

    def "http call with given transaction id"() {
        given:
        RestTemplate restTemplate = new RestTemplate()
        MultiValueMap headers = new LinkedMultiValueMap()
        headers.add(TRANSACTION_ID_HEADER, "test-123")
        RequestEntity requestEntity = new RequestEntity(null, headers, HttpMethod.GET, URI.create("http://localhost:$port/"))

        when:
        restTemplate.exchange(requestEntity, Void)

        then:
        thrown(HttpClientErrorException.NotFound)
    }
}
