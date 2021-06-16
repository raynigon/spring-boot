package com.raynigon.ecs.logging.access.server;

import org.apache.catalina.AccessLog;
import org.apache.catalina.Valve;

public interface AccessValve extends Valve, AccessLog {
}
