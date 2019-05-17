package uk.gov.justice.services.core.lifecycle.events.catchup;

import java.time.ZonedDateTime;
import java.util.Objects;

public class CatchupRequestedEvent {

    private final String initiatedBy;
    private final ZonedDateTime catchupRequestedAt;

    public CatchupRequestedEvent(final String initiatedBy, final ZonedDateTime catchupRequestedAt) {
        this.initiatedBy = initiatedBy;
        this.catchupRequestedAt = catchupRequestedAt;
    }

    public String getInitiatedBy() {
        return initiatedBy;
    }

    public ZonedDateTime getCatchupRequestedAt() {
        return catchupRequestedAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof CatchupRequestedEvent)) return false;
        final CatchupRequestedEvent that = (CatchupRequestedEvent) o;
        return Objects.equals(initiatedBy, that.initiatedBy) &&
                Objects.equals(catchupRequestedAt, that.catchupRequestedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(initiatedBy, catchupRequestedAt);
    }

    @Override
    public String toString() {
        return "CatchupRequestedEvent{" +
                "initiatedBy='" + initiatedBy + '\'' +
                ", catchupRequestedAt=" + catchupRequestedAt +
                '}';
    }
}
