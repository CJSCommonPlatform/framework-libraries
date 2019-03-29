package uk.gov.justice.services.core.lifecycle.events.shuttering;

import java.time.ZonedDateTime;
import java.util.Objects;

public class ShutteringRequestedEvent {

    private final String initiatedBy;
    private final ZonedDateTime shutteringRequestedAt;

    public ShutteringRequestedEvent(final String initiatedBy, final ZonedDateTime shutteringRequestedAt) {
        this.initiatedBy = initiatedBy;
        this.shutteringRequestedAt = shutteringRequestedAt;
    }

    public Object getInitiatedBy() {
        return initiatedBy;
    }

    public ZonedDateTime getShutteringRequestedAt() {
        return shutteringRequestedAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof ShutteringRequestedEvent)) return false;
        final ShutteringRequestedEvent that = (ShutteringRequestedEvent) o;
        return Objects.equals(initiatedBy, that.initiatedBy) &&
                Objects.equals(shutteringRequestedAt, that.shutteringRequestedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(initiatedBy, shutteringRequestedAt);
    }

    @Override
    public String toString() {
        return "ShutteringRequestedEvent{" +
                "initiatedBy='" + initiatedBy + '\'' +
                ", shutteringRequestedAt=" + shutteringRequestedAt +
                '}';
    }
}
