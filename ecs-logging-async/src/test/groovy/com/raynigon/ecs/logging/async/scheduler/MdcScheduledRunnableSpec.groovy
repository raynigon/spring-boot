package com.raynigon.ecs.logging.async.scheduler

import static com.raynigon.ecs.logging.LoggingConstants.TRANSACTION_ID_PROPERTY

import org.slf4j.MDC
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicBoolean

class MdcScheduledRunnableSpec extends Specification {

    def 'transaction id is set'() {
        given:
        AtomicBoolean result = new AtomicBoolean(false)
        Runnable source = { result.set(MDC.get(TRANSACTION_ID_PROPERTY) != null) }
        MdcScheduledRunnable target = new MdcScheduledRunnable(source)

        when:
        target.run()

        then:
        result.get()
    }

    def 'restore MDC context after run'() {
        given:
        AtomicBoolean result = new AtomicBoolean(false)
        Runnable source = { result.set(MDC.get("test") == null) }
        MdcScheduledRunnable target = new MdcScheduledRunnable(source)

        and:
        MDC.clear()
        MDC.put("test", "123")

        when:
        target.run()

        then:
        result.get()
        MDC.get("test") == "123"
    }
}
