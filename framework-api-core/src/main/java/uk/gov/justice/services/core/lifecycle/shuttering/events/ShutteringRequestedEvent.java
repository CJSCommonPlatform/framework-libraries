package uk.gov.justice.services.core.lifecycle.shuttering.events;

import java.time.ZonedDateTime;
import java.util.Objects;

public class ShutteringRequestedEvent {

    private final Object caller;
    private final ZonedDateTime shutteringRequestedAt;

    public ShutteringRequestedEvent(final Object caller, final ZonedDateTime shutteringRequestedAt) {
        this.caller = caller;
        this.shutteringRequestedAt = shutteringRequestedAt;
    }

    public Object getCaller() {
        return caller;
    }

    public ZonedDateTime getShutteringRequestedAt() {
        return shutteringRequestedAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof ShutteringRequestedEvent)) return false;
        final ShutteringRequestedEvent that = (ShutteringRequestedEvent) o;
        return Objects.equals(caller, that.caller) &&
                Objects.equals(shutteringRequestedAt, that.shutteringRequestedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(caller, shutteringRequestedAt);
    }

    @Override
    public String toString() {
        return "ShutteringRequestedEvent{" +
                "caller=" + caller +
                ", shutteringRequestedAt=" + shutteringRequestedAt +
                '}';
    }
}
