package com.raynigon.ecs.logging.application;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.encoder.EncoderBase;
import com.raynigon.ecs.logging.EcsEncoder;
import com.raynigon.ecs.logging.application.converter.EcsApplicationConverter;
import com.raynigon.ecs.logging.application.event.EcsApplicationLogEvent;

public class EcsApplicationEncoder extends EncoderBase<ILoggingEvent> {

    private final EcsEncoder<ILoggingEvent, EcsApplicationLogEvent> encoder = new EcsEncoder<>(new EcsApplicationConverter());

    @Override
    public byte[] encode(ILoggingEvent event) {
        return encoder.encode(event);
    }

    @Override
    public byte[] headerBytes() {
        return new byte[0];
    }

    @Override
    public byte[] footerBytes() {
        return new byte[0];
    }
}
