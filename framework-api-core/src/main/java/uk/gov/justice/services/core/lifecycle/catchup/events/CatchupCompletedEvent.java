package uk.gov.justice.services.core.lifecycle.catchup.events;

import java.time.ZonedDateTime;
import java.util.Objects;

public class CatchupCompletedEvent {

    private final long currentEventNumber;
    private final int totalNumberOfEvents;
    private final ZonedDateTime catchupCompletedAt;

    public CatchupCompletedEvent(
            final long currentEventNumber,
            final int totalNumberOfEvents,
            final ZonedDateTime catchupCompletedAt) {
        this.currentEventNumber = currentEventNumber;
        this.totalNumberOfEvents = totalNumberOfEvents;
        this.catchupCompletedAt = catchupCompletedAt;
    }

    public int getTotalNumberOfEvents() {
        return totalNumberOfEvents;
    }

    public long getCurrentEventNumber() {
        return currentEventNumber;
    }

    public ZonedDateTime getCatchupCompletedAt() {
        return catchupCompletedAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof CatchupCompletedEvent)) return false;
        final CatchupCompletedEvent that = (CatchupCompletedEvent) o;
        return currentEventNumber == that.currentEventNumber &&
                totalNumberOfEvents == that.totalNumberOfEvents &&
                Objects.equals(catchupCompletedAt, that.catchupCompletedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentEventNumber, totalNumberOfEvents, catchupCompletedAt);
    }

    @Override
    public String toString() {
        return "CatchupCompletedEvent{" +
                "currentEventNumber=" + currentEventNumber +
                ", totalNumberOfEvents=" + totalNumberOfEvents +
                ", catchupCompletedAt=" + catchupCompletedAt +
                '}';
    }
}
