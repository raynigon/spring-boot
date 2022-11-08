package com.raynigon.ecs.logging.async.service.helper;

import io.micrometer.core.instrument.Timer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class MicrometerTimer implements TimerWrapper{

    private final Timer timer;

    @Override
    @SneakyThrows
    public <V> V recordCallable(Callable<V> callable) {
        return timer.recordCallable(callable);
    }

    @Override
    public <U> U record(Supplier<U> supplier) {
        return timer.record(supplier);
    }

    @Override
    public SampleWrapper start() {
        return wrap(timer, Timer.start());
    }

    @Override
    public long stop(SampleWrapper wrapper) {
        MicrometerSample sample = (MicrometerSample) wrapper;
        return sample.stop();
    }

    private SampleWrapper wrap(Timer timer, Timer.Sample sample) {
        return new MicrometerSample(timer, sample);
    }
}
