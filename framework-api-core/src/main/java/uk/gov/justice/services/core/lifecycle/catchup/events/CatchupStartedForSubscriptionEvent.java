package uk.gov.justice.services.core.lifecycle.catchup.events;

import java.time.ZonedDateTime;
import java.util.Objects;

public class CatchupStartedForSubscriptionEvent {

    private final String subscriptionName;
    private final ZonedDateTime catchupStartedAt;

    public CatchupStartedForSubscriptionEvent(final String subscriptionName, final ZonedDateTime catchupStartedAt) {
        this.subscriptionName = subscriptionName;
        this.catchupStartedAt = catchupStartedAt;
    }

    public String getSubscriptionName() {
        return subscriptionName;
    }

    public ZonedDateTime getCatchupStartedAt() {
        return catchupStartedAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof CatchupStartedForSubscriptionEvent)) return false;
        final CatchupStartedForSubscriptionEvent that = (CatchupStartedForSubscriptionEvent) o;
        return Objects.equals(subscriptionName, that.subscriptionName) &&
                Objects.equals(catchupStartedAt, that.catchupStartedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subscriptionName, catchupStartedAt);
    }

    @Override
    public String toString() {
        return "CatchupStartedForSubscriptionEvent{" +
                "subscriptionName='" + subscriptionName + '\'' +
                ", catchupStartedAt=" + catchupStartedAt +
                '}';
    }
}
