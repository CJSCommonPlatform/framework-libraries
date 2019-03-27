package uk.gov.justice.services.core.lifecycle.events.catchup;

import java.time.ZonedDateTime;
import java.util.Objects;

public class CatchupRequestedEvent {
    private final Object caller;
    private final ZonedDateTime catchupRequestedAt;

    public CatchupRequestedEvent(final Object caller, final ZonedDateTime catchupRequestedAt) {
        this.caller = caller;
        this.catchupRequestedAt = catchupRequestedAt;
    }

    public Object getCaller() {
        return caller;
    }

    public ZonedDateTime getCatchupRequestedAt() {
        return catchupRequestedAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof CatchupRequestedEvent)) return false;
        final CatchupRequestedEvent that = (CatchupRequestedEvent) o;
        return Objects.equals(caller, that.caller) &&
                Objects.equals(catchupRequestedAt, that.catchupRequestedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(caller, catchupRequestedAt);
    }

    @Override
    public String toString() {
        return "CatchupRequestedEvent{" +
                "caller=" + caller +
                ", catchupRequestedAt=" + catchupRequestedAt +
                '}';
    }
}
