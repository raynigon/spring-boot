package com.raynigon.ecs.logging.application.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.raynigon.ecs.logging.event.EcsLogEvent;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Map;

import static com.raynigon.ecs.logging.LoggingConstants.ECS_VERSION;

@Data
@Builder(toBuilder = true)
public class EcsApplicationLogEvent implements EcsLogEvent {
    @Builder.Default
    @JsonProperty("ecs.version")
    private final String version = ECS_VERSION;

    @Builder.Default
    @JsonProperty("event.dataset")
    private final String eventDataset = "application";

    @JsonProperty("@timestamp")
    private final OffsetDateTime timestamp;

    @JsonProperty("service.name")
    private final String serviceName;

    @JsonProperty("trace.id")
    private final String traceId;

    @JsonProperty("message")
    private final String message;

    @JsonProperty("log.level")
    private final String level;

    @JsonProperty("log.logger")
    private final String logger;

    @JsonProperty("error.type")
    private final String errorType;

    @JsonProperty("error.message")
    private final String errorMessage;

    @JsonProperty("error.stack_trace")
    private final String errorStackTrace;

    @JsonProperty("process.thread.name")
    private final String threadName;

    @JsonProperty("labels")
    private final Map<String, String> labels;
}