package com.raynigon.ecs.logging.access.client;

import com.raynigon.ecs.logging.LoggingConstants;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.Server;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;

import static com.raynigon.ecs.logging.LoggingConstants.TRANSACTION_ID_HEADER;
import static com.raynigon.ecs.logging.LoggingConstants.TRANSACTION_ID_PROPERTY;

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
            String correlationId = MDC.get(TRANSACTION_ID_PROPERTY);
            if (correlationId == null) {
                correlationId = UUID.randomUUID().toString();
            }
            if (!request.getHeaders().containsKey(TRANSACTION_ID_HEADER)) {
                request.getHeaders().add(TRANSACTION_ID_HEADER, correlationId);
            }
            return execution.execute(request, body);
        });
    }
}
