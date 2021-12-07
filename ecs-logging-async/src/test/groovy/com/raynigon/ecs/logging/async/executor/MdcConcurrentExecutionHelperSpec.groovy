package com.raynigon.ecs.logging.async.executor


import org.slf4j.MDC
import spock.lang.Specification

class MdcConcurrentExecutionHelperSpec extends Specification {

    def 'mdc context gets replaced'() {
        given:
        MDC.clear()
        MDC.put("test", "old")
        Map newContext = ["test": "new"]

        when:
        def previous = MdcConcurrentExecutionHelper.beforeExecution(newContext)

        then:
        previous.containsKey("test")
        previous["test"] == "old"

        and:
        MDC.get("test") == "new"
    }

    def 'mdc context gets cleared before execution'() {
        given:
        MDC.clear()
        MDC.put("test", "old")

        when:
        def previous = MdcConcurrentExecutionHelper.beforeExecution(null)

        then:
        previous.containsKey("test")
        previous["test"] == "old"

        and:
        MDC.get("test") == null
    }

    def 'mdc context gets restored'() {
        given:
        MDC.clear()
        MDC.put("test", "new")
        Map oldContext = ["test": "old"]

        when:
        MdcConcurrentExecutionHelper.afterExecution(oldContext)

        then:
        MDC.get("test") == "old"
    }

    def 'mdc context gets cleared after execution'() {
        given:
        MDC.clear()
        MDC.put("test", "new")

        when:
        MdcConcurrentExecutionHelper.afterExecution(null)

        then:
        MDC.get("test") == null
    }
}
