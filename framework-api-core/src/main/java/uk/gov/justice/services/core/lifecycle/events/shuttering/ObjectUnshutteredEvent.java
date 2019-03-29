package uk.gov.justice.services.core.lifecycle.events.shuttering;

import java.time.ZonedDateTime;
import java.util.Objects;

public class ObjectUnshutteredEvent {

    private final String initiatedBy;
    private final ZonedDateTime objectUnshutteredAt;

    public ObjectUnshutteredEvent(final String initiatedBy, final ZonedDateTime objectUnshutteredAt) {
        this.initiatedBy = initiatedBy;
        this.objectUnshutteredAt = objectUnshutteredAt;
    }

    public String getInitiatedBy() {
        return initiatedBy;
    }

    public ZonedDateTime getObjectUnshutteredAt() {
        return objectUnshutteredAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof ObjectUnshutteredEvent)) return false;
        final ObjectUnshutteredEvent that = (ObjectUnshutteredEvent) o;
        return Objects.equals(initiatedBy, that.initiatedBy) &&
                Objects.equals(objectUnshutteredAt, that.objectUnshutteredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(initiatedBy, objectUnshutteredAt);
    }

    @Override
    public String toString() {
        return "ObjectUnshutteredEvent{" +
                "initiatedBy='" + initiatedBy + '\'' +
                ", objectUnshutteredAt=" + objectUnshutteredAt +
                '}';
    }
}
