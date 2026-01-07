package com.raynigon.ecs.logging.application

import tools.jackson.databind.ObjectMapper

import static com.raynigon.ecs.logging.LoggingConstants.SERVICE_NAME_PROPERTY

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.classic.spi.LoggingEvent
import spock.lang.Specification

class EcsApplicationEncoderSpec extends Specification {

    private final EcsApplicationEncoder encoder = new EcsApplicationEncoder()

    def "encoding with minimal event"() {
        given:
        LoggerContext context = new LoggerContext()
        context.putProperty(SERVICE_NAME_PROPERTY, "my-service")
        Logger logger = new Logger("logger.class", null, context)
        ILoggingEvent event = new LoggingEvent("logger.class", logger, Level.INFO, "", null, null)
        event.setMDCPropertyMap([:])

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
        result["service.name"] == "my-service"
    }
}
