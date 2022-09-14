package com.raynigon.ecs.logging.async.scheduler

import com.raynigon.ecs.logging.async.model.MdcRunnable

import static com.raynigon.ecs.logging.LoggingConstants.TRANSACTION_ID_PROPERTY

import nl.altindag.log.LogCaptor
import org.slf4j.MDC
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicBoolean

class MdcRunnableSpec extends Specification {

    def 'transaction id is set'() {
        given:
        AtomicBoolean result = new AtomicBoolean(false)
        Runnable source = { result.set(MDC.get(TRANSACTION_ID_PROPERTY) != null) }
        MdcRunnable target = new MdcRunnable(source)

        when:
        target.run()

        then:
        result.get()
    }

    def 'restore MDC context after run'() {
        given:
        AtomicBoolean result = new AtomicBoolean(false)
        Runnable source = { result.set(MDC.get("test") == null) }
        MdcRunnable target = new MdcRunnable(source)

        and:
        MDC.clear()
        MDC.put("test", "123")

        when:
        target.run()

        then:
        result.get()
        MDC.get("test") == "123"
    }

    def 'restore MDC context on exception'() {
        given:
        AtomicBoolean result = new AtomicBoolean(false)
        Runnable source = { result.set(MDC.get("test") == null); throw new IllegalStateException("test") }
        MdcRunnable target = new MdcRunnable(source)

        and:
        LogCaptor logCaptor = LogCaptor.forClass(MdcRunnable)

        and:
        MDC.clear()
        MDC.put("test", "123")

        when:
        target.run()

        then:
        thrown(MdcScheduledRunnableException)

        and:
        result.get()
        MDC.get("test") == "123"

        and:
        logCaptor.getErrorLogs().size() == 1
    }
}
