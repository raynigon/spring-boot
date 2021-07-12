package com.raynigon.ecs.logging.event;

import java.time.OffsetDateTime;

public interface EcsLogEvent {

    OffsetDateTime getTimestamp();

    String getVersion();

    String getServiceName();

    String getEventDataset();

    String getTransactionId();

    String getSessionId();
}
