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
}
