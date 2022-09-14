package com.raynigon.ecs.logging.async

import static com.raynigon.ecs.logging.LoggingConstants.TRANSACTION_ID_PROPERTY

import com.raynigon.ecs.logging.async.service.AsyncService
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

@EnableAutoConfiguration
@ContextConfiguration(classes = MdcExecutorConfiguration)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = [])
class MdcExecutorConfigurationITSpec extends Specification {

    @Autowired
    AsyncService asyncService

    @Autowired
    ThreadPoolTaskExecutor taskExecutor

    def 'setup application'() {
        expect:
        true
    }

    def 'mdc tag passthroughs works for supplyAsync'() {
        given:
        MDC.put("test", "123")
        AtomicInteger holder = new AtomicInteger()

        when:
        asyncService.supplyAsync({ holder.set(Integer.parseInt(MDC.get("test"))) }).get()

        then:
        MDC.get("test") == "123"
        holder.get() == 123
    }

    def 'mdc tag passthroughs works for submit'() {
        given:
        MDC.put("test", "456")
        AtomicInteger holder = new AtomicInteger()

        when:
        asyncService.submit({ holder.set(Integer.parseInt(MDC.get("test"))) }).get()

        then:
        MDC.get("test") == "456"
        holder.get() == 456
    }

    def 'mdc tag passthroughs works for submit'() {
        given:
        MDC.put("test", "456")
        AtomicInteger holder = new AtomicInteger()

        when:
        asyncService.submit({ holder.set(Integer.parseInt(MDC.get("test"))) }).get()

        then:
        MDC.get("test") == "456"
        holder.get() == 456
    }

    def 'task executor creates new context'() {
        given:
        MDC.clear()
        AtomicReference<String> holder = new AtomicReference<String>()

        expect:
        MDC.get(TRANSACTION_ID_PROPERTY) == null

        when:
        def future = taskExecutor.submit({ holder.set(MDC.get(TRANSACTION_ID_PROPERTY)) })

        then:
        MDC.get(TRANSACTION_ID_PROPERTY) == null
        future.get() == null
        holder.get() != null
    }
}
