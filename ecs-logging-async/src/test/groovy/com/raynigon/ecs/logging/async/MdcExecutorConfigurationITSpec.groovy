package com.raynigon.ecs.logging.async

import com.raynigon.ecs.logging.async.service.AsyncService
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicInteger

@EnableAutoConfiguration
@ContextConfiguration(classes = MdcExecutorConfiguration)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = [])
class MdcExecutorConfigurationITSpec extends Specification {

    @Autowired
    AsyncService asyncService

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
}
