package com.raynigon.ecs.logging.async.service.helper;

import lombok.SneakyThrows;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class NoOpTimer implements TimerWrapper {
    @Override
    @SneakyThrows
    public <V> V recordCallable(Callable<V> callable) {
        return callable.call();
    }

    @Override
    public <U> U record(Supplier<U> supplier) {
        return supplier.get();
    }

    @Override
    public SampleWrapper start() {
        return new NoOpSample();
    }

    @Override
    public long stop(SampleWrapper wrapper) {
        return -1;
    }
}
