package com.raynigon.ecs.logging.access.helper

import ch.qos.logback.access.spi.IAccessEvent
import com.raynigon.ecs.logging.access.EcsAccessEncoder

import java.time.Instant
import java.time.OffsetDateTime

class RecordingEcsAccessEncoder extends EcsAccessEncoder {

    private static final List<EventRecord> records = new ArrayList<>()

    static clearRecords() {
        synchronized (records) {
            records.clear()
        }
    }

    static getRecords() {
        return Collections.unmodifiableList(records)
    }

    @Override
    byte[] encode(IAccessEvent event) {
        byte[] result = super.encode(event)
        synchronized (records) {
            records.add(new EventRecord(this, event, result))
        }
        return result
    }

    class EventRecord {

        public final Instant timestamp
        public final EcsAccessEncoder encoder
        public final IAccessEvent event
        public final byte[] result

        EventRecord(EcsAccessEncoder encoder, IAccessEvent event, byte[] result) {
            this.timestamp = Instant.now()
            this.encoder = encoder
            this.event = event
            this.result = result
        }
    }
}
