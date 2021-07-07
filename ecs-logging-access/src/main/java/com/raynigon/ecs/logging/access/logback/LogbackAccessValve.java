package com.raynigon.ecs.logging.access.logback;

import ch.qos.logback.access.tomcat.TomcatServerAdapter;
import com.raynigon.ecs.logging.access.context.IAccessLogContext;
import com.raynigon.ecs.logging.access.event.EcsAccessEvent;
import com.raynigon.ecs.logging.access.server.AccessValve;
import lombok.SneakyThrows;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.Valve;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;
import org.slf4j.MDC;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.UUID;

import static com.raynigon.ecs.logging.LoggingConstants.SERVICE_NAME_PROPERTY;
import static com.raynigon.ecs.logging.LoggingConstants.CORRELATION_ID_HEADER;
import static com.raynigon.ecs.logging.LoggingConstants.CORRELATION_ID_PROPERTY;

public class LogbackAccessValve extends ValveBase implements AccessValve, Lifecycle {

    private boolean started = false;
    private final IAccessLogContext context;
    private boolean requestAttributesEnabled = true;

    public LogbackAccessValve(String serviceName) {
        context = new AccessLogContext();
        context.putProperty(SERVICE_NAME_PROPERTY, serviceName);
    }

    @Override
    public void log(Request request, Response response, long time) {
        TomcatServerAdapter adapter = new TomcatServerAdapter(request, response);
        EcsAccessEvent event = new EcsAccessEvent(request, response, adapter, context);
        context.appendEvent(event);
    }

    @Override
    public void setRequestAttributesEnabled(boolean requestAttributesEnabled) {
        this.requestAttributesEnabled = requestAttributesEnabled;
    }

    @Override
    public boolean getRequestAttributesEnabled() {
        return requestAttributesEnabled;
    }

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        String correlationId = request.getHeader(CORRELATION_ID_HEADER);
        if (correlationId == null)
            correlationId = UUID.randomUUID().toString();

        response.addHeader(CORRELATION_ID_HEADER, correlationId);

        MDC.put(CORRELATION_ID_PROPERTY, correlationId);
        Valve next = getNext();
        if (next != null) {
            next.invoke(request, response);
        }
        MDC.remove(CORRELATION_ID_PROPERTY);
    }

    public boolean isStarted() {
        return started;
    }

    @Override
    @SneakyThrows
    protected synchronized void startInternal() {
        context.start();
        started = true;
        super.startInternal();
    }

    @Override
    @SneakyThrows
    protected synchronized void stopInternal() {
        context.stop();
        started = false;
        super.stopInternal();
    }
}
