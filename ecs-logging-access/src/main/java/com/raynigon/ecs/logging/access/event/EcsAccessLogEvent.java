package com.raynigon.ecs.logging.access.event;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raynigon.ecs.logging.event.EcsLogEvent;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.OffsetDateTime;

import static com.raynigon.ecs.logging.LoggingConstants.ECS_VERSION;

@Data
@Builder(toBuilder = true)
public final class EcsAccessLogEvent implements EcsLogEvent {
    @Builder.Default
    @JsonProperty("ecs.version")
    private final String version = ECS_VERSION;

    @Builder.Default
    @JsonProperty("event.dataset")
    private final String eventDataset = "access";

    @JsonProperty("@timestamp")
    private final OffsetDateTime timestamp;

    @JsonProperty("service.name")
    private final String serviceName;

    @JsonProperty("transaction.id")
    private final String transactionId;

    @JsonProperty("session.id")
    private final String sessionId;

    @JsonProperty("client.address")
    private final String clientAddress;

    @JsonProperty("user.name")
    private final String userName;

    @JsonProperty("http.request.method")
    private final String requestMethod;

    @JsonProperty("url.path")
    private final String urlPath;

    @JsonProperty("url.query")
    private final String urlQuery;

    @JsonProperty("http.response.status_code")
    private final int responseStatus;

    @JsonProperty("http.response.body.bytes")
    private final Long responseSize;

    @JsonProperty("user_agent.original")
    private final String userAgent;

    @JsonProperty("http.request.body.content")
    private final String requestBody;

    @JsonProperty("http.response.body.content")
    private final String responseBody;

    @JsonIgnore
    private final Duration duration;

    @JsonProperty("event.duration")
    public Long getDuration() {
        if (duration == null) return null;
        return duration.toMillis();
    }


}
