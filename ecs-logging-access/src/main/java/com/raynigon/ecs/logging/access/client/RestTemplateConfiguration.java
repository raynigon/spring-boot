package com.raynigon.ecs.logging.access.client;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@ConditionalOnClass(value = {RestTemplate.class})
public class RestTemplateConfiguration {

    private final List<RestTemplate> templates;

    @PostConstruct
    public void configure() {
        templates.forEach(this::configure);
    }

    public void configure(RestTemplate restTemplate) {
        restTemplate.getInterceptors().add((HttpRequest request, byte[] body, ClientHttpRequestExecution execution) -> {
            new AccessLogHeaderFilter().accept(request.getHeaders());
            return execution.execute(request, body);
        });
    }
}
