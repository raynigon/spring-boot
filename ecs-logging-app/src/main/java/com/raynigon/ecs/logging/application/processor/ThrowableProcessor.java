package com.raynigon.ecs.logging.application.processor;

import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxy;
import com.raynigon.ecs.logging.application.event.EcsApplicationLogEvent;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class ThrowableProcessor implements ApplicationEventProcessor {

    private final ThrowableProxyConverter throwableProxyConverter = new ThrowableProxyConverter();


    @Override
    public EcsApplicationLogEvent process(EcsApplicationLogEvent result, ILoggingEvent event) {
        if (event.getThrowableProxy() == null) return result;

        IThrowableProxy throwableProxy = event.getThrowableProxy();
        String stackTrace;
        if (throwableProxy instanceof ThrowableProxy) {
            stackTrace = ExceptionUtils.getStackTrace(((ThrowableProxy) throwableProxy).getThrowable());
        } else {
            stackTrace = throwableProxyConverter.convert(event);
        }
        return result.toBuilder()
                .errorType(throwableProxy.getClassName())
                .errorMessage(throwableProxy.getMessage())
                .errorStackTrace(stackTrace)
                .build();
    }
}
