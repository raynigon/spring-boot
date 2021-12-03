package com.raynigon.ecs.logging.access;

import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.core.encoder.EncoderBase;
import com.raynigon.ecs.logging.EcsEncoder;
import com.raynigon.ecs.logging.access.converter.EcsAccessConverter;
import com.raynigon.ecs.logging.access.event.EcsAccessLogEvent;

public class EcsAccessEncoder extends EncoderBase<IAccessEvent> {

    private final EcsEncoder<IAccessEvent, EcsAccessLogEvent> encoder = new EcsEncoder<>(new EcsAccessConverter(getContext()));

    @Override
    public byte[] encode(IAccessEvent event) {
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
