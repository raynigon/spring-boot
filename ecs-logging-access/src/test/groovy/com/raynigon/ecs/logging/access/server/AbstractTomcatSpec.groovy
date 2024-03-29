package com.raynigon.ecs.logging.access.server

import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus

import static com.raynigon.ecs.logging.LoggingConstants.SESSION_ID_HEADER
import static com.raynigon.ecs.logging.LoggingConstants.TRANSACTION_ID_HEADER

import com.raynigon.ecs.logging.access.helper.RecordingEcsAccessEncoder
import com.raynigon.ecs.logging.access.helper.Wait
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import java.time.Duration

abstract class AbstractTomcatSpec extends Specification {

    @LocalServerPort
    int port = 0

    abstract void prepare()

    def "http call with given session id"() {
        given:
        RecordingEcsAccessEncoder.clearRecords()

        and:
        RestTemplate restTemplate = new RestTemplate()
        MultiValueMap headers = new LinkedMultiValueMap()
        headers.add(SESSION_ID_HEADER, "test-123")
        RequestEntity requestEntity = new RequestEntity(null, headers, HttpMethod.GET, URI.create("http://localhost:$port/"))

        when:
        def result = restTemplate.exchange(requestEntity, String)

        then:
        result.statusCode == HttpStatus.OK

        and:
        Wait.wait(Duration.ofMillis(100), { RecordingEcsAccessEncoder.records.size() == 1 })
        new String(RecordingEcsAccessEncoder.records.get(0).result).contains("\"session.id\":\"test-123\"")
    }

    def "http call with given transaction id"() {
        given:
        RecordingEcsAccessEncoder.clearRecords()

        and:
        RestTemplate restTemplate = new RestTemplate()
        MultiValueMap headers = new LinkedMultiValueMap()
        headers.add(TRANSACTION_ID_HEADER, "test-123")
        RequestEntity requestEntity = new RequestEntity(null, headers, HttpMethod.GET, URI.create("http://localhost:$port/"))

        when:
        def result = restTemplate.exchange(requestEntity, String)

        then:
        result.statusCode == HttpStatus.OK

        and:
        Wait.wait(Duration.ofMillis(100), { RecordingEcsAccessEncoder.records.size() == 1 })
        new String(RecordingEcsAccessEncoder.records.get(0).result).contains("\"transaction.id\":\"test-123\"")
    }
}
