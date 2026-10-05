package com.raynigon.ecs.logging.access.server;

import com.raynigon.ecs.logging.access.AccessLogProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.server.autoconfigure.servlet.ServletWebServerConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
@AutoConfiguration
@Order(Ordered.HIGHEST_PRECEDENCE)
@AutoConfigureBefore(ServletWebServerConfiguration.class)
@ConditionalOnClass(value = {WebServerFactoryCustomizer.class, RequestMappingHandlerMapping.class})
@EnableConfigurationProperties(AccessLogProperties.class)
public class AccessLogFilterConfiguration {


    @Bean
    @NonNull
    @ConditionalOnProperty(value = "raynigon.logging.access.export-body", havingValue = "true")
    public FilterRegistrationBean<EcsAccessLoggingFilter> requestLoggingFilter(
            AccessLogProperties properties,
            ObjectProvider<RequestMappingHandlerMapping> requestMappingHandlerMapping) {
        HandlerLoggingAnnotationLookup annotationLookup =
                new HandlerLoggingAnnotationLookup(requestMappingHandlerMapping.getIfAvailable());
        return new FilterRegistrationBean<>(new EcsAccessLoggingFilter(properties, annotationLookup));
    }
}
