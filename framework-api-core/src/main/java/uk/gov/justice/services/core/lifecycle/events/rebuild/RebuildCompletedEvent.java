package uk.gov.justice.services.core.lifecycle.events.rebuild;

import java.time.ZonedDateTime;
import java.util.Objects;

public class RebuildCompletedEvent {

    private final ZonedDateTime rebuildCompletedAt;

    public RebuildCompletedEvent(final ZonedDateTime rebuildCompletedAt) {
        this.rebuildCompletedAt = rebuildCompletedAt;
    }

    public ZonedDateTime getRebuildCompletedAt() {
        return rebuildCompletedAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final RebuildCompletedEvent that = (RebuildCompletedEvent) o;
        return Objects.equals(rebuildCompletedAt, that.rebuildCompletedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rebuildCompletedAt);
    }

    @Override
    public String toString() {
        return "RebuildCompletedEvent{" +
                "rebuildCompletedAt=" + rebuildCompletedAt +
                '}';
    }
}
