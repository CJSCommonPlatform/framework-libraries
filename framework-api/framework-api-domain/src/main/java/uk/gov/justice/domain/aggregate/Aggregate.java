package uk.gov.justice.domain.aggregate;

import static java.util.stream.Collectors.toList;

import java.io.Serializable;
import java.util.stream.Stream;

/**
 * Underlying interface that every domain aggregate needs to implement.
 */
public interface Aggregate extends Serializable {

    /**
     * Apply an event to update the state of this aggregate. The provided event should be returned
     * by the call to support chaining.
     *
     * @param event the event to apply
     * @return the applied event
     */
    Object apply(final Object event);

    /**
     * Apply a stream of events to update the state of this aggregate. The events are returned as a
     * new stream to support chaining.
     *
     * @param events the events to apply
     * @return the stream of events applied
     */
    default Stream<Object> apply(final Stream<Object> events) {
        return events
                .map(this::apply)
                .collect(toList())
                .stream();
    }

    /**
     * Apply a stream of events to update the state of this aggregate.
     *
     * @param events the events to apply
     */
    default void applyForEach(final Stream<Object> events) {
        events.forEach(this::apply);
    }
}
