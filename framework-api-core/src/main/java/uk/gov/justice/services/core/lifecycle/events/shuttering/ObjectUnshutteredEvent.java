package uk.gov.justice.services.core.lifecycle.events.shuttering;


import java.time.ZonedDateTime;
import java.util.Objects;

public class ObjectUnshutteredEvent {

    private final Shutterable shutterable;
    private final ZonedDateTime objectUnshutteredAt;

    public ObjectUnshutteredEvent(final Shutterable shutterable, final ZonedDateTime objectUnshutteredAt) {
        this.shutterable = shutterable;
        this.objectUnshutteredAt = objectUnshutteredAt;
    }

    public Shutterable getShutterable() {
        return shutterable;
    }

    public ZonedDateTime getObjectUnshutteredAt() {
        return objectUnshutteredAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof ObjectUnshutteredEvent)) return false;
        final ObjectUnshutteredEvent that = (ObjectUnshutteredEvent) o;
        return Objects.equals(shutterable, that.shutterable) &&
                Objects.equals(objectUnshutteredAt, that.objectUnshutteredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shutterable, objectUnshutteredAt);
    }

    @Override
    public String toString() {
        return "ObjectUnshutteredEvent{" +
                "shutterable=" + shutterable +
                ", objectUnshutteredAt=" + objectUnshutteredAt +
                '}';
    }
}
