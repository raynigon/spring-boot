package com.raynigon.ecs.logging.access.context;

import com.raynigon.ecs.logging.access.AccessLogProperties;
import com.raynigon.ecs.logging.access.event.EcsAccessEvent;

public interface IAccessLogContext {

    String getProperty(String key);

    void putProperty(String key, String value);

    void appendEvent(EcsAccessEvent event);

    void start();

    void stop();

    AccessLogProperties getConfig();
}
