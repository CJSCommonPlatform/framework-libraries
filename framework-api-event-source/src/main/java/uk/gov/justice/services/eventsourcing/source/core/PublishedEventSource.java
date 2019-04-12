package uk.gov.justice.services.eventsourcing.source.core;

import uk.gov.justice.services.messaging.JsonEnvelope;

import java.util.stream.Stream;

/**
 * Source of published events.
 */
public interface PublishedEventSource {

    /**
     * returns a (Java) stream of all events since the provided event number. That is all events
     * who's eventNumber is greater than the provided event number
     *
     * @param eventNumber An event number to search from
     * @return a Java Stream of Events
     */
    Stream<JsonEnvelope> findEventsSince(final long eventNumber);
}
