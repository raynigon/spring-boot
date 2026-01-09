package com.raynigon.ecs.logging;

import ch.qos.logback.core.Context;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.raynigon.ecs.logging.converter.EventConverter;
import com.raynigon.ecs.logging.converter.LogbackConverter;
import com.raynigon.ecs.logging.event.EcsLogEvent;
import lombok.SneakyThrows;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.util.StdDateFormat;

import java.nio.charset.StandardCharsets;

public class EcsEncoder<I, O extends EcsLogEvent> {

    private final EventConverter<I, O> converter;
    private final JsonMapper jsonMapper;

    public EcsEncoder(EventConverter<I, O> converter) {
        this.converter = converter;

        this.jsonMapper = JsonMapper.builder()
                .changeDefaultPropertyInclusion((JsonInclude.Value a) -> JsonInclude.Value.construct(JsonInclude.Include.NON_NULL, JsonInclude.Include.NON_NULL))
                .defaultDateFormat((new StdDateFormat()).withColonInTimeZone(true))
                .build();
    }

    public void setupLogback(Context context) {
        if (converter instanceof LogbackConverter) {
            ((LogbackConverter) converter).setup(context);
        }
    }

    public byte[] encode(I event) {
        EcsLogEvent ecsEvent = converter.convert(event);
        return writeEvent(ecsEvent);
    }

    @SneakyThrows
    private byte[] writeEvent(EcsLogEvent event) {
        byte[] jsonb = jsonMapper.writeValueAsBytes(event);
        byte[] newLine = "\n".getBytes(StandardCharsets.UTF_8);
        byte[] result = new byte[jsonb.length + newLine.length];
        System.arraycopy(jsonb, 0, result, 0, jsonb.length);
        System.arraycopy(newLine, 0, result, jsonb.length, newLine.length);
        return result;
    }
}