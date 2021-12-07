package com.raynigon.ecs.logging.access.client

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
}
