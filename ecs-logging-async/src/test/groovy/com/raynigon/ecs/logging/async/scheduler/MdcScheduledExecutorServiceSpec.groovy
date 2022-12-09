package com.raynigon.ecs.logging.async.scheduler

import com.raynigon.ecs.logging.async.model.MdcRunnable
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.Trigger
import spock.lang.Specification
import spock.lang.Subject

import java.time.Duration
import java.time.Instant

class MdcScheduledExecutorServiceSpec extends Specification {

    TaskScheduler delegate = Mock()

    @Subject
    MdcScheduledExecutorService executorService = new MdcScheduledExecutorService(delegate)

    def 'schedule with trigger gets delegated'() {
        given:
        Runnable task = {}
        Trigger trigger = Mock()

        when:
        executorService.schedule(task, trigger)

        then:
        1 * delegate.schedule(_ as MdcRunnable, _ as Trigger)
    }

    def 'schedule with date gets delegated'() {
        given:
        Runnable task = {}
        Instant instant = Instant.now()

        when:
        executorService.schedule(task, instant)

        then:
        1 * delegate.schedule(_ as MdcRunnable, _ as Instant)
    }

    def 'scheduleAtFixedRate with date gets delegated and period'() {
        given:
        Runnable task = {}
        Instant instant = Instant.now()

        when:
        executorService.scheduleAtFixedRate(task, instant, Duration.ZERO)

        then:
        1 * delegate.scheduleAtFixedRate(_ as MdcRunnable, _ as Instant, Duration.ZERO)
    }

    def 'scheduleAtFixedRate with period'() {
        given:
        Runnable task = {}

        when:
        executorService.scheduleAtFixedRate(task, Duration.ZERO)

        then:
        1 * delegate.scheduleAtFixedRate(_ as MdcRunnable, Duration.ZERO)
    }

    def 'scheduleWithFixedDelay with date and delay'() {
        given:
        Runnable task = {}
        Instant instant = Instant.now()

        when:
        executorService.scheduleWithFixedDelay(task, instant, Duration.ZERO)

        then:
        1 * delegate.scheduleWithFixedDelay(_ as MdcRunnable, _ as Instant, Duration.ZERO)
    }

    def 'scheduleWithFixedDelay with delay'() {
        given:
        Runnable task = {}

        when:
        executorService.scheduleWithFixedDelay(task, Duration.ZERO)

        then:
        1 * delegate.scheduleWithFixedDelay(_ as MdcRunnable, Duration.ZERO)
    }
}
