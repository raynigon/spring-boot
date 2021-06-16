package com.raynigon.ecs.logging.access.server;

import com.raynigon.ecs.logging.access.logback.LogbackAccessValve;
import org.apache.catalina.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@AutoConfigureBefore(ServletWebServerFactoryAutoConfiguration.class)
@ConditionalOnClass(value = {Server.class, WebServerFactoryCustomizer.class})
public class TomcatAccessLogConfiguration {

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatAccessLogCustomizer(List<AccessValve> accessValves) {
        return factory -> factory.addContextValves(accessValves.toArray(AccessValve[]::new));
    }

    @Bean
    public AccessValve accessLogValve() {
        return new LogbackAccessValve(applicationName);
    }
}
