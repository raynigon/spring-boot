package com.raynigon.ecs.logging.access

import com.raynigon.ecs.logging.access.server.TomcatAccessLogConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

@EnableAutoConfiguration
@ContextConfiguration(classes = TomcatAccessLogConfiguration)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = ["spring.application.name=my-test-app"])
class TomcatITSpec extends Specification {

    @LocalServerPort
    int port = 0

    def "startup application"() {
        given:
        RestTemplate restTemplate = new RestTemplate()

        when:
        restTemplate.getForEntity("http://localhost:$port/", Void)

        then:
        thrown(HttpClientErrorException.NotFound)
    }
}
