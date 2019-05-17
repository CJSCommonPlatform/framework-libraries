package uk.gov.justice.services.core.lifecycle.events.rebuild;

import java.time.ZonedDateTime;
import java.util.Objects;

public class RebuildStartedEvent {

    private final ZonedDateTime rebuildStartedAt;

    public RebuildStartedEvent(final ZonedDateTime rebuildStartedAt) {
        this.rebuildStartedAt = rebuildStartedAt;
    }

    public ZonedDateTime getRebuildStartedAt() {
        return rebuildStartedAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final RebuildStartedEvent that = (RebuildStartedEvent) o;
        return Objects.equals(rebuildStartedAt, that.rebuildStartedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rebuildStartedAt);
    }

    @Override
    public String toString() {
        return "RebuildStartedEvent{" +
                "rebuildStartedAt=" + rebuildStartedAt +
                '}';
    }
}
