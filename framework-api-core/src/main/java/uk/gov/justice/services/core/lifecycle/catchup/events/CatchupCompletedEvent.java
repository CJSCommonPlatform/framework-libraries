package uk.gov.justice.services.core.lifecycle.catchup.events;

import java.time.ZonedDateTime;
import java.util.Objects;

public class CatchupCompletedEvent {

    private final ZonedDateTime catchupCompletedAt;

    public CatchupCompletedEvent(final ZonedDateTime catchupCompletedAt) {
        this.catchupCompletedAt = catchupCompletedAt;
    }

    public ZonedDateTime getCatchupCompletedAt() {
        return catchupCompletedAt;
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof CatchupCompletedEvent)) return false;
        final CatchupCompletedEvent that = (CatchupCompletedEvent) o;
        return Objects.equals(catchupCompletedAt, that.catchupCompletedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(catchupCompletedAt);
    }

    @Override
    public String toString() {
        return "CatchupCompletedEvent{" +
                "catchupCompletedAt=" + catchupCompletedAt +
                '}';
    }
}
