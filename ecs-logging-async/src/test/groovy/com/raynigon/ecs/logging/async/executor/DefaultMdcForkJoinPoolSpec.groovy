package com.raynigon.ecs.logging.async.executor

import com.raynigon.ecs.logging.async.helper.DummyForkJoinTask
import com.raynigon.ecs.logging.async.helper.Wait
import spock.lang.Specification
import spock.lang.Subject

import java.time.Duration
import java.util.concurrent.Callable
import java.util.concurrent.ForkJoinTask
import java.util.concurrent.atomic.AtomicReference

class DefaultMdcForkJoinPoolSpec extends Specification {

    @Subject
    DefaultMdcForkJoinPool pool = new DefaultMdcForkJoinPool()

    def 'submit callable'() {
        given:
        AtomicReference result = new AtomicReference()
        Callable callable = { result.set("executed") }

        when:
        pool.submit(callable).get()

        then:
        result.get() == "executed"
    }

    def 'submit runnable'() {
        given:
        AtomicReference result = new AtomicReference()
        Runnable runnable = { result.set("executed") }

        when:
        pool.submit(runnable).get()

        then:
        result.get() == "executed"
    }

    def 'submit runnable with default result'() {
        given:
        AtomicReference result = new AtomicReference()
        String defaultResult = "default"
        Runnable runnable = { result.set("normal"); return "normal" }

        when:
        pool.submit(runnable, defaultResult).get()

        then:
        result.get() == "normal"
    }

    def 'submit task'() {
        given:
        ForkJoinTask task = new DummyForkJoinTask()

        when:
        ForkJoinTask result = pool.submit(task)

        then:
        result instanceof MdcForkJoinTask
    }

    def 'execute runnable'() {
        given:
        AtomicReference result = new AtomicReference()
        Runnable runnable = { result.set("executed") }

        when:
        pool.execute(runnable)

        then:
        Wait.wait(Duration.ofMillis(100), { result.get() == "executed" })
    }

    def 'execute task'() {
        given:
        AtomicReference result = new AtomicReference()
        ForkJoinTask task = new DummyForkJoinTask()
        task.runnable = { result.set("executed") }

        when:
        pool.execute(task)

        then:
        Wait.wait(Duration.ofMillis(100), { result.get() == "executed" })
    }
}
