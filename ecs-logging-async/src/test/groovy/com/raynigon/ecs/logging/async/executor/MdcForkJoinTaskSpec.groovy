package com.raynigon.ecs.logging.async.executor

import com.raynigon.ecs.logging.async.helper.DummyForkJoinTask
import com.raynigon.ecs.logging.async.helper.PublicMdcForkJoinTask
import org.slf4j.MDC
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicReference

class MdcForkJoinTaskSpec extends Specification {

    def 'mdc context is restored after execution'() {
        given:
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
        MDC.put("test", "old")
        DummyForkJoinTask dummyTask = new DummyForkJoinTask()
        AtomicReference reference = new AtomicReference()
        dummyTask.runnable = {
            reference.set(MDC.get("test"))
            throw new RuntimeException("test exception")
        }
        Map newContext = ["test": "new"]
        PublicMdcForkJoinTask task = new PublicMdcForkJoinTask(dummyTask, newContext)

        when:
        task.exec()

        then:
        thrown(RuntimeException)

        and:
        MDC.get("test") == "old"
        reference.get() == "new"
    }
}
