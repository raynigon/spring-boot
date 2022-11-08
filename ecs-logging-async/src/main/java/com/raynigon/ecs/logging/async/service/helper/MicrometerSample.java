package com.raynigon.ecs.logging.async.service.helper;

import io.micrometer.core.instrument.Timer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MicrometerSample implements SampleWrapper {

    private final Timer timer;
    private final Timer.Sample sample;

    public long stop() {
        return sample.stop(timer);
    }
}
