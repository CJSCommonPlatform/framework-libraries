package uk.gov.justice.services.core.lifecycle.events.shuttering;

import java.time.ZonedDateTime;
import java.util.Objects;

public class ObjectShutteredEvent {

    private final Shutterable shutterable;
    private final ZonedDateTime objectShutteredAt;

    public ObjectShutteredEvent(final Shutterable shutterable, final ZonedDateTime objectShutteredAt) {
        this.shutterable = shutterable;
        this.objectShutteredAt = objectShutteredAt;
    }

    public Shutterable getShutterable() {
        return shutterable;
    }

    public ZonedDateTime getObjectShutteredAt() {
        return objectShutteredAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof ObjectShutteredEvent)) return false;
        final ObjectShutteredEvent that = (ObjectShutteredEvent) o;
        return Objects.equals(shutterable, that.shutterable) &&
                Objects.equals(objectShutteredAt, that.objectShutteredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shutterable, objectShutteredAt);
    }

    @Override
    public String toString() {
        return "ObjectShutteredEvent{" +
                "shutterable=" + shutterable +
                ", objectShutteredAt=" + objectShutteredAt +
                '}';
    }
}


