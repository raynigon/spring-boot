package com.raynigon.ecs.logging.access.server

import com.raynigon.ecs.logging.access.helper.EchoController
import com.raynigon.ecs.logging.access.helper.RecordingEcsAccessEncoder
import com.raynigon.ecs.logging.access.helper.Wait
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.client.RestTemplate

import java.time.Duration

@DirtiesContext
@EnableAutoConfiguration
@ContextConfiguration(classes = [TomcatAccessLogConfiguration, EchoController])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = [
        "spring.application.name=my-test-app",
        "raynigon.logging.access.export-body=true"
])
class AbstractTomcatWithBodyITSpec extends AbstractTomcatSpec {

    @Override
    void prepare() {
        RecordingEcsAccessEncoder.clearRecords()
    }

    def "http call without headers"() {
        given:
        RestTemplate restTemplate = new RestTemplate()
        RecordingEcsAccessEncoder.clearRecords()

        when:
        def result = restTemplate.getForEntity("http://localhost:$port/", String)

        then:
        result.statusCodeValue == 200
        result.body == "Hello World!"

        and:
        Wait.wait(Duration.ofMillis(100), { RecordingEcsAccessEncoder.records.size() == 1 })
        new String(RecordingEcsAccessEncoder.records.get(0).result).contains("\"http.request.method\":\"GET\"")
        new String(RecordingEcsAccessEncoder.records.get(0).result).contains("\"http.response.status_code\":200")
        new String(RecordingEcsAccessEncoder.records.get(0).result).contains("\"url.path\":\"/\"")
        new String(RecordingEcsAccessEncoder.records.get(0).result).contains("\"http.response.body.bytes\":12")
        new String(RecordingEcsAccessEncoder.records.get(0).result).contains("\"http.response.body.content\":\"Hello World!\"")
    }

    def "http call with body"() {
        given:
        RestTemplate restTemplate = new RestTemplate()

        when:
        def result = restTemplate.postForEntity("http://localhost:$port/echo", ["test": "123"], Map)

        then:
        result.statusCodeValue == 200
        result.body == ["test": "123"]
    }
}
