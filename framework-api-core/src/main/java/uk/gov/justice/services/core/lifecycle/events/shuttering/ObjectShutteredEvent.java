package uk.gov.justice.services.core.lifecycle.events.shuttering;

import java.time.ZonedDateTime;
import java.util.Objects;

public class ObjectShutteredEvent {

    private final String initiatedBy;
    private final ZonedDateTime objectShutteredAt;

    public ObjectShutteredEvent(final String initiatedBy, final ZonedDateTime objectShutteredAt) {
        this.initiatedBy = initiatedBy;
        this.objectShutteredAt = objectShutteredAt;
    }

    public String getInitiatedBy() {
        return initiatedBy;
    }

    public ZonedDateTime getObjectShutteredAt() {
        return objectShutteredAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof ObjectShutteredEvent)) return false;
        final ObjectShutteredEvent that = (ObjectShutteredEvent) o;
        return Objects.equals(initiatedBy, that.initiatedBy) &&
                Objects.equals(objectShutteredAt, that.objectShutteredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(initiatedBy, objectShutteredAt);
    }

    @Override
    public String toString() {
        return "ObjectShutteredEvent{" +
                "initiatedBy='" + initiatedBy + '\'' +
                ", objectShutteredAt=" + objectShutteredAt +
                '}';
    }
}


