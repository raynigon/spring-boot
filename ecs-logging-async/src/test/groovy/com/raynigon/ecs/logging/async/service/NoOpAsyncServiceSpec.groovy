package com.raynigon.ecs.logging.async.service

import spock.lang.Specification
import spock.lang.Subject

import java.util.concurrent.Callable
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Supplier

class NoOpAsyncServiceSpec extends Specification {

    @Subject
    AsyncService service = new NoOpAsyncService()

    def 'supplyAsync gets executed'() {
        given:
        AtomicInteger changed = new AtomicInteger(0)
        Supplier supplier = { changed.set(123); changed.get() }

        when:
        def result = service.supplyAsync(supplier).get()

        then:
        result == 123
        changed.get() == 123
    }

    def 'submit gets executed'() {
        given:
        AtomicInteger changed = new AtomicInteger(0)
        Callable callable = { changed.set(123) }

        when:
        service.submit(callable).get()

        then:
        changed.get() == 123
    }
}
