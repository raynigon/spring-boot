package com.raynigon.ecs.logging;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.raynigon.ecs.logging.converter.EventConverter;
import com.raynigon.ecs.logging.event.EcsLogEvent;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;

public class EcsEncoder<I, O extends EcsLogEvent> {

    private final EventConverter<I, O> converter;
    private final ObjectMapper objectMapper;

    public EcsEncoder(EventConverter<I, O> converter) {
        this.converter = converter;

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.objectMapper.setDateFormat((new StdDateFormat()).withColonInTimeZone(true));
    }

    public byte[] encode(I event) {
        EcsLogEvent ecsEvent = converter.convert(event);
        return writeEvent(ecsEvent);
    }

    @SneakyThrows
    private byte[] writeEvent(EcsLogEvent event) {
        byte[] jsonb = objectMapper.writeValueAsBytes(event);
        byte[] newLine = "\n".getBytes(StandardCharsets.UTF_8);
        byte[] result = new byte[jsonb.length + newLine.length];
        System.arraycopy(jsonb, 0, result, 0, jsonb.length);
        System.arraycopy(newLine, 0, result, jsonb.length, newLine.length);
        return result;
    }
}