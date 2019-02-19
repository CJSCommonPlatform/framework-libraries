package uk.gov.justice.services.core.lifecycle.shuttering.events;

import java.time.ZonedDateTime;
import java.util.Objects;

public class UnshutteringRequestedEvent {

    private final Object caller;
    private final ZonedDateTime unshutteringRequestedAt;

    public UnshutteringRequestedEvent(final Object caller, final ZonedDateTime unshutteringRequestedAt) {
        this.caller = caller;
        this.unshutteringRequestedAt = unshutteringRequestedAt;
    }

    public Object getCaller() {
        return caller;
    }

    public ZonedDateTime getUnshutteringRequestedAt() {
        return unshutteringRequestedAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof UnshutteringRequestedEvent)) return false;
        final UnshutteringRequestedEvent that = (UnshutteringRequestedEvent) o;
        return Objects.equals(caller, that.caller) &&
                Objects.equals(unshutteringRequestedAt, that.unshutteringRequestedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(caller, unshutteringRequestedAt);
    }

    @Override
    public String toString() {
        return "UnshutteringRequestedEvent{" +
                "caller=" + caller +
                ", unshutteringRequestedAt=" + unshutteringRequestedAt +
                '}';
    }
}
