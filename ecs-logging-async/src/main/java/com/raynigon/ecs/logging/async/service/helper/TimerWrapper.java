package com.raynigon.ecs.logging.async.service.helper;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public interface TimerWrapper {
    <V> V recordCallable(Callable<V> callable);

    <U> U record(Supplier<U> supplier);

    SampleWrapper start();

    long stop(SampleWrapper wrapper);
}
