package uk.gov.justice.services.core.lifecycle.events.shuttering;

import java.time.ZonedDateTime;
import java.util.Objects;

public class UnshutteringRequestedEvent {

    private final String initiatedBy;
    private final ZonedDateTime unshutteringRequestedAt;

    public UnshutteringRequestedEvent(final String initiatedBy, final ZonedDateTime unshutteringRequestedAt) {
        this.initiatedBy = initiatedBy;
        this.unshutteringRequestedAt = unshutteringRequestedAt;
    }

    public String getInitiatedBy() {
        return initiatedBy;
    }

    public ZonedDateTime getUnshutteringRequestedAt() {
        return unshutteringRequestedAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof UnshutteringRequestedEvent)) return false;
        final UnshutteringRequestedEvent that = (UnshutteringRequestedEvent) o;
        return Objects.equals(initiatedBy, that.initiatedBy) &&
                Objects.equals(unshutteringRequestedAt, that.unshutteringRequestedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(initiatedBy, unshutteringRequestedAt);
    }

    @Override
    public String toString() {
        return "UnshutteringRequestedEvent{" +
                "initiatedBy='" + initiatedBy + '\'' +
                ", unshutteringRequestedAt=" + unshutteringRequestedAt +
                '}';
    }
}
