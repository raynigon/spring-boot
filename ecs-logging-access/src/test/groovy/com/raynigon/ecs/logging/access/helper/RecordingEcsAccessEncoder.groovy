package com.raynigon.ecs.logging.access.helper

import ch.qos.logback.access.common.spi.IAccessEvent
import com.raynigon.ecs.logging.access.EcsAccessEncoder

import java.time.Instant

class RecordingEcsAccessEncoder extends EcsAccessEncoder {

    private static final List<EventRecord> RECORDS = []

    static clearRecords() {
        synchronized (RECORDS) {
            RECORDS.clear()
        }
    }

    static getRecords() {
        return Collections.unmodifiableList(RECORDS)
    }

    @Override
    byte[] encode(IAccessEvent event) {
        byte[] result = super.encode(event)
        synchronized (RECORDS) {
            RECORDS.add(new EventRecord(this, event, result))
        }
        return result
    }

    class EventRecord {

        private final Instant timestamp
        private final EcsAccessEncoder encoder
        private final IAccessEvent event
        private final byte[] result

        EventRecord(EcsAccessEncoder encoder, IAccessEvent event, byte[] result) {
            this.timestamp = Instant.now()
            this.encoder = encoder
            this.event = event
            this.result = result
        }

        byte[] getResult() {
            return result
        }
    }
}
