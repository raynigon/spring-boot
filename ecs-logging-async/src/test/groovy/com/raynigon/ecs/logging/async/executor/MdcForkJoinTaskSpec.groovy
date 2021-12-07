package com.raynigon.ecs.logging.async.executor

import com.raynigon.ecs.logging.async.helper.DummyForkJoinTask
import com.raynigon.ecs.logging.async.helper.PublicMdcForkJoinTask
import org.slf4j.MDC
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicReference

class MdcForkJoinTaskSpec extends Specification {

    def 'mdc context is restored after execution'() {
        given:
        MDC.clear()
        MDC.put("test", "old")
        DummyForkJoinTask dummyTask = new DummyForkJoinTask()
        AtomicReference reference = new AtomicReference()
        dummyTask.runnable = { reference.set(MDC.get("test")) }
        Map newContext = ["test": "new"]
        PublicMdcForkJoinTask task = new PublicMdcForkJoinTask(dummyTask, newContext)

        when:
        task.exec()

        then:
        MDC.get("test") == "old"
        reference.get() == "new"
    }

    def 'mdc context is restored on exception'() {
        given:
        MDC.clear()
        MDC.put("test", "old")
        DummyForkJoinTask dummyTask = new DummyForkJoinTask()
        AtomicReference reference = new AtomicReference()
        dummyTask.runnable = {
            reference.set(MDC.get("test"))
            throw new IllegalStateException("test exception")
        }
        Map newContext = ["test": "new"]
        PublicMdcForkJoinTask task = new PublicMdcForkJoinTask(dummyTask, newContext)

        when:
        task.exec()

        then:
        thrown(IllegalStateException)

        and:
        MDC.get("test") == "old"
        reference.get() == "new"
    }

    def 'raw result handling returns expected result'() {
        given:
        DummyForkJoinTask dummyTask = new DummyForkJoinTask()
        PublicMdcForkJoinTask task = new PublicMdcForkJoinTask(dummyTask, [:])
        dummyTask.runnable = { dummyTask.setRawResult("123") }

        when:
        task.exec()

        then:
        dummyTask.getRawResult() == "123"
        task.getRawResult() == "123"
    }

    def 'raw result handling with override'() {
        given:
        DummyForkJoinTask dummyTask = new DummyForkJoinTask()
        PublicMdcForkJoinTask task = new PublicMdcForkJoinTask(dummyTask, [:])
        dummyTask.runnable = { task.setRawResult("123") }

        when:
        task.exec()

        then:
        dummyTask.getRawResult() == null
        task.getRawResult() == "123"
    }
}
