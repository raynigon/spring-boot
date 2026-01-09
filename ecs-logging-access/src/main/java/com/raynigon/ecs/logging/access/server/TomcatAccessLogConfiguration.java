package com.raynigon.ecs.logging.access.server;

import com.raynigon.ecs.logging.access.AccessLogProperties;
import com.raynigon.ecs.logging.access.logback.LogbackAccessValve;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.server.autoconfigure.servlet.ServletWebServerConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.tomcat.servlet.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
@AutoConfiguration
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
@AutoConfigureBefore(ServletWebServerConfiguration.class)
@ConditionalOnClass(value = {Server.class, WebServerFactoryCustomizer.class})
@EnableConfigurationProperties(AccessLogProperties.class)
public class TomcatAccessLogConfiguration {

    @Value("${spring.application.name}")
    private String applicationName;

    private final AccessLogProperties config;

    @Bean
    public WebServerFactoryCustomizer<@NonNull TomcatServletWebServerFactory> tomcatAccessLogCustomizer(List<AccessValve> accessValves) {
        return factory -> factory.addContextValves(accessValves.toArray(AccessValve[]::new));
    }

    @Bean
    public AccessValve accessLogValve() {
        return new LogbackAccessValve(config, applicationName);
    }
}
