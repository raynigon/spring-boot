package com.raynigon.ecs.logging.access.context;

import ch.qos.logback.core.Context;
import com.raynigon.ecs.logging.access.AccessLogProperties;
import com.raynigon.ecs.logging.access.event.EcsAccessEvent;

public interface IAccessLogContext extends Context {

    String getProperty(String key);

    void putProperty(String key, String value);

    void appendEvent(EcsAccessEvent event);

    void start();

    void stop();

    AccessLogProperties getConfig();
}
