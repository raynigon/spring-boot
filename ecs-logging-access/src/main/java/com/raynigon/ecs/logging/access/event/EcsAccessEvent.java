package com.raynigon.ecs.logging.access.event;

import ch.qos.logback.access.spi.AccessEvent;
import ch.qos.logback.access.spi.ServerAdapter;
import com.raynigon.ecs.logging.access.context.IAccessLogContext;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

@Getter
public class EcsAccessEvent extends AccessEvent {

    private final IAccessLogContext context;
    private final Duration duration;

    public EcsAccessEvent(
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse,
            ServerAdapter adapter,
            IAccessLogContext context,
            Duration duration
    ) {
        super(httpRequest, httpResponse, adapter);
        this.context = context;
        this.duration = duration;
    }
}
