package com.raynigon.ecs.logging.application

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.classic.spi.LoggingEvent
import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification


class EcsApplicationEncoderSpec extends Specification {

    private EcsApplicationEncoder encoder = new EcsApplicationEncoder()

    def "encoding with minimal event"() {
        given:
        LoggerContext context = new LoggerContext()
        Logger logger = new Logger("logger.class", null, context)
        ILoggingEvent event = new LoggingEvent("logger.class", logger, Level.INFO, "", null, null)

        when:
        def binaryResult = encoder.encode(event)
        def result = (new ObjectMapper()).readValue(binaryResult, Map)

        then:
        result.containsKey("ecs.version")
        result.containsKey("event.dataset")
        result.containsKey("@timestamp")
        result.containsKey("service.name")
        result.containsKey("message")

        and:
        result["ecs.version"] == "1.7"
        result["event.dataset"] == "application"
        result["service.name"] == "spring-application"
    }
}
