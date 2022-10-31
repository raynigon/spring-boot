package com.raynigon.ecs.logging.async.service

import com.raynigon.ecs.logging.async.executor.MdcForkJoinPool
import com.raynigon.ecs.logging.async.helper.DummyForkJoinTask
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import spock.lang.Specification
import spock.lang.Subject

import java.util.concurrent.Callable
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Supplier

class DefaultAsyncServiceSpec extends Specification {

    MdcForkJoinPool pool = Mock()

    SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();

    @Subject
    AsyncService service = new DefaultAsyncService(pool, meterRegistry)

    def 'supplyAsync gets executed'() {
        given:
        AtomicInteger changed = new AtomicInteger(0)
        Supplier supplier = { changed.set(123); changed.get() }

        when:
        def result = service.supplyAsync(supplier).get()

        then:
        result == 123
        changed.get() == 123

        and:
        1 * pool.execute(_ as Runnable) >> { Runnable r -> r.run() }

        and:
        meterRegistry.find(DefaultAsyncService.TIMER_NAME).timer().count() == 1
    }

    def 'submit gets executed'() {
        given:
        AtomicInteger changed = new AtomicInteger(0)
        Callable callable = { changed.set(123) }

        and:
        DummyForkJoinTask task = new DummyForkJoinTask()

        when:
        service.submit(callable).get()

        then:
        changed.get() == 123

        and:
        1 * pool.submit(_ as Callable) >> { Callable c ->
            c.call()
            task.setRawResult(123)
            task.invoke()
            return task
        }

        and:
        meterRegistry.find(DefaultAsyncService.TIMER_NAME).timer().count() == 1
    }
}
