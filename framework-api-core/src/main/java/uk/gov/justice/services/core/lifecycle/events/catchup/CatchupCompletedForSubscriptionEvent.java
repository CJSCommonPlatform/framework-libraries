package uk.gov.justice.services.core.lifecycle.events.catchup;

import java.time.ZonedDateTime;
import java.util.Objects;

public class CatchupCompletedForSubscriptionEvent {

    private final String subscriptionName;
    private final int totalNumberOfEvents;
    private final ZonedDateTime catchupCompletedAt;

    public CatchupCompletedForSubscriptionEvent(
            final String subscriptionName,
            final int totalNumberOfEvents,
            final ZonedDateTime catchupCompletedAt) {
        this.subscriptionName = subscriptionName;
        this.totalNumberOfEvents = totalNumberOfEvents;
        this.catchupCompletedAt = catchupCompletedAt;
    }

    public String getSubscriptionName() {
        return subscriptionName;
    }

    public int getTotalNumberOfEvents() {
        return totalNumberOfEvents;
    }

    public ZonedDateTime getCatchupCompletedAt() {
        return catchupCompletedAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof CatchupCompletedForSubscriptionEvent)) return false;
        final CatchupCompletedForSubscriptionEvent that = (CatchupCompletedForSubscriptionEvent) o;
        return totalNumberOfEvents == that.totalNumberOfEvents &&
                Objects.equals(subscriptionName, that.subscriptionName) &&
                Objects.equals(catchupCompletedAt, that.catchupCompletedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subscriptionName, totalNumberOfEvents, catchupCompletedAt);
    }

    @Override
    public String toString() {
        return "CatchupCompletedForSubscriptionEvent{" +
                "subscriptionName='" + subscriptionName + '\'' +
                ", totalNumberOfEvents=" + totalNumberOfEvents +
                ", catchupCompletedAt=" + catchupCompletedAt +
                '}';
    }
}
