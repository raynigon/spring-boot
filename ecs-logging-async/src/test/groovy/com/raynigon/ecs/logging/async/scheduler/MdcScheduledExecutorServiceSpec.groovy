package com.raynigon.ecs.logging.async.scheduler

import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.Trigger
import spock.lang.Specification
import spock.lang.Subject

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
        1 * delegate.schedule(_ as MdcScheduledRunnable, _ as Trigger)
    }

    def 'schedule with date gets delegated'() {
        given:
        Runnable task = {}
        Date date = Date.from(Instant.now())

        when:
        executorService.schedule(task, date)

        then:
        1 * delegate.schedule(_ as MdcScheduledRunnable, _ as Date)
    }

    def 'scheduleAtFixedRate with date gets delegated and period'() {
        given:
        Runnable task = {}
        Date date = Date.from(Instant.now())

        when:
        executorService.scheduleAtFixedRate(task, date, 0L)

        then:
        1 * delegate.scheduleAtFixedRate(_ as MdcScheduledRunnable, _ as Date, 0L)
    }

    def 'scheduleAtFixedRate with period'() {
        given:
        Runnable task = {}

        when:
        executorService.scheduleAtFixedRate(task, 0L)

        then:
        1 * delegate.scheduleAtFixedRate(_ as MdcScheduledRunnable, 0L)
    }

    def 'scheduleWithFixedDelay with date and delay'() {
        given:
        Runnable task = {}
        Date date = Date.from(Instant.now())

        when:
        executorService.scheduleWithFixedDelay(task, date, 0L)

        then:
        1 * delegate.scheduleWithFixedDelay(_ as MdcScheduledRunnable, _ as Date, 0L)
    }

    def 'scheduleWithFixedDelay with delay'() {
        given:
        Runnable task = {}

        when:
        executorService.scheduleWithFixedDelay(task, 0L)

        then:
        1 * delegate.scheduleWithFixedDelay(_ as MdcScheduledRunnable, 0L)
    }
}
