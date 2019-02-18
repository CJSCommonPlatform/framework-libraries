package uk.gov.justice.services.core.lifecycle.catchup.events;

import java.time.ZonedDateTime;
import java.util.Objects;

public class CatchupStartedEvent {

    private final ZonedDateTime catchupStartedAt;

    public CatchupStartedEvent(final ZonedDateTime catchupStartedAt) {
        this.catchupStartedAt = catchupStartedAt;
    }

    public ZonedDateTime getCatchupStartedAt() {
        return catchupStartedAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof CatchupStartedEvent)) return false;
        final CatchupStartedEvent that = (CatchupStartedEvent) o;
        return Objects.equals(catchupStartedAt, that.catchupStartedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(catchupStartedAt);
    }

    @Override
    public String toString() {
        return "CatchupStartedEvent{" +
                "catchupStartedAt=" + catchupStartedAt +
                '}';
    }
}
