package com.raynigon.ecs.logging.access.server

import com.raynigon.ecs.logging.access.helper.MultipartAndAnnotationEchoController
import com.raynigon.ecs.logging.access.helper.RecordingEcsAccessEncoder
import com.raynigon.ecs.logging.access.helper.Wait
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import java.time.Duration

@DirtiesContext
@EnableAutoConfiguration
@ContextConfiguration(classes = [TomcatAccessLogConfiguration, MultipartAndAnnotationEchoController])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = [
        "spring.application.name=my-test-app",
        "raynigon.logging.access.export-body=true"
])
class AbstractTomcatWithMultipartITSpec extends Specification {

    @LocalServerPort
    int port = 0

    def "multipart upload succeeds instead of failing with a 500, and the access log entry is still written without the raw body"() {
        given:
        RecordingEcsAccessEncoder.clearRecords()
        RestTemplate restTemplate = new RestTemplate()

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>()
        HttpHeaders fileHeaders = new HttpHeaders()
        fileHeaders.setContentType(MediaType.IMAGE_PNG)
        body.add("file", new HttpEntity<>(new NamedByteArrayResource("test.png", [0x89, 0x50, 0x4E, 0x47] as byte[]), fileHeaders))
        body.add("meta", "some metadata")

        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(MediaType.MULTIPART_FORM_DATA)
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers)

        when:
        def result = restTemplate.postForEntity("http://localhost:$port/multipart", requestEntity, Map)

        then: "the request succeeds instead of failing with a 500"
        result.statusCode == HttpStatus.OK
        result.body.meta == "some metadata"
        result.body.fileSize == 4

        and: "the access log entry is still written"
        Wait.wait(Duration.ofMillis(300), { RecordingEcsAccessEncoder.records.size() == 1 })
        def logLine = new String(RecordingEcsAccessEncoder.records.get(0).result)
        logLine.contains("\"http.response.status_code\":200")
        logLine.contains("\"url.path\":\"/multipart\"")

        and: "but the raw multipart body (boundaries, part headers, binary bytes) is not included"
        logLine.contains("\"http.request.body.content\":\"\"")
        !logLine.contains("Content-Disposition")
        !logLine.contains("some metadata")
    }

    def "a method annotated with @EcsSkipBodyLogging keeps the access log entry but omits the body"() {
        given:
        RecordingEcsAccessEncoder.clearRecords()
        RestTemplate restTemplate = new RestTemplate()

        when:
        def result = restTemplate.postForEntity("http://localhost:$port/annotated/skip-body", "secret-payload", String)

        then:
        result.statusCode == HttpStatus.OK
        result.body == "secret-payload"

        and:
        Wait.wait(Duration.ofMillis(300), { RecordingEcsAccessEncoder.records.size() == 1 })
        def logLine = new String(RecordingEcsAccessEncoder.records.get(0).result)
        logLine.contains("\"url.path\":\"/annotated/skip-body\"")
        logLine.contains("\"http.request.body.content\":\"\"")
        logLine.contains("\"http.response.body.content\":\"\"")
        !logLine.contains("secret-payload")
    }

    def "a method annotated with @EcsSkipAccessLogging suppresses the access log entry entirely"() {
        given:
        RecordingEcsAccessEncoder.clearRecords()
        RestTemplate restTemplate = new RestTemplate()

        when:
        def result = restTemplate.postForEntity("http://localhost:$port/annotated/skip-log", "secret-payload", String)

        then: "the call itself still succeeds normally"
        result.statusCode == HttpStatus.OK
        result.body == "secret-payload"

        and: "no access log entry is written for it"
        !Wait.wait(Duration.ofMillis(300), { !RecordingEcsAccessEncoder.records.isEmpty() })
        RecordingEcsAccessEncoder.records.isEmpty()
    }

    private static class NamedByteArrayResource extends ByteArrayResource {
        private final String filename

        NamedByteArrayResource(String filename, byte[] content) {
            super(content)
            this.filename = filename
        }

        @Override
        String getFilename() {
            return filename
        }
    }
}
