package com.raynigon.ecs.logging.access.client;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.boot.restclient.RestClientCustomizer;

@Configuration
@AutoConfiguration
@RequiredArgsConstructor
@ConditionalOnClass(value = {RestClient.Builder.class})
public class RestClientConfiguration implements RestClientCustomizer {

    @Override
    public void customize(RestClient.Builder restClientBuilder) {
        restClientBuilder.requestInterceptor((request, body, execution) -> {
            new AccessLogHeaderFilter().accept(request.getHeaders());
            return execution.execute(request, body);
        });
    }
}
