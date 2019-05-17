package uk.gov.justice.services.core.lifecycle.events.rebuild;

import java.time.ZonedDateTime;
import java.util.Objects;

public class RebuildRequestedEvent {

    private final String initiatedBy;
    private final ZonedDateTime rebuildRequestedAt;

    public RebuildRequestedEvent(final String initiatedBy, final ZonedDateTime rebuildRequestedAt) {
        this.initiatedBy = initiatedBy;
        this.rebuildRequestedAt = rebuildRequestedAt;
    }

    public String getInitiatedBy() {
        return initiatedBy;
    }

    public ZonedDateTime getRebuildRequestedAt() {
        return rebuildRequestedAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final RebuildRequestedEvent that = (RebuildRequestedEvent) o;
        return Objects.equals(initiatedBy, that.initiatedBy) &&
                Objects.equals(rebuildRequestedAt, that.rebuildRequestedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(initiatedBy, rebuildRequestedAt);
    }

    @Override
    public String toString() {
        return "RebuildRequestedEvent{" +
                "initiatedBy='" + initiatedBy + '\'' +
                ", rebuildRequestedAt=" + rebuildRequestedAt +
                '}';
    }
}
