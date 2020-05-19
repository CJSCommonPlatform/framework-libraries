package uk.gov.justice.services.eventsourcing.source.core;


import uk.gov.justice.services.eventsourcing.source.core.exception.EventStreamException;
import uk.gov.justice.services.messaging.JsonEnvelope;

import java.util.UUID;
import java.util.stream.Stream;

/**
 * Event stream that can be read from and appended to.
 */
public interface EventStream {

    /**
     * Get the stream of events.
     *
     * @return the stream of events
     */
    Stream<JsonEnvelope> read();

    /**
     * Get the stream of events from the given version.
     *
     * @param version the version of the stream
     * @return the stream of events
     */
    Stream<JsonEnvelope> readFrom(final long version);

    /**
     * Get the stream of events from the given version.
     *
     * @param version the version of the stream
     * @param pageSize the size of the page of the result set
     * @return the stream of events
     */
    Stream<JsonEnvelope> readFrom(final long version, final int pageSize);

    /**
     * Store a stream of events.
     *
     * @param events the stream of events to store
     * @return the current stream version
     * @throws EventStreamException if an event could not be appended
     */
    long append(final Stream<JsonEnvelope> events) throws EventStreamException;

    /**
     * Store a stream of events.
     *
     * @param stream    the stream of events to store
     * @param tolerance - tolerance for optimistic lock errors. <ul> <li>CONSECUTIVE - store the
     *                  given stream of events with consecutive versions only, fail in case of an
     *                  optimistic lock.</li> <li>NON_CONSECUTIVE - allows to store the given stream
     *                  of events with non consecutive version ids, but reduces the risk of throwing
     *                  optimistic lock error in case of a version conflict.</li></ul>
     * @return the current stream version
     * @throws EventStreamException if an event could not be appended
     */
    long append(final Stream<JsonEnvelope> stream, final Tolerance tolerance) throws EventStreamException;

    /**
     * Store a stream of events after the given version.
     *
     * @param events  the stream of events to store
     * @param version the version to append from
     * @return the current stream version
     * @throws EventStreamException if an event could not be appended
     */
    long appendAfter(final Stream<JsonEnvelope> events, final long version) throws EventStreamException;

    /**
     * Get the size of the event stream.
     *
     * Can be used to determine the position of the next event when appending to the stream
     *
     * @return the size of the steam; zero when stream is empty
     */
    long size();

    /**
     * Retrieve the id of this stream.
     *
     * @return the stream id.
     */
    UUID getId();

    /**
     * Get the position of this event stream within the sequence of all streams.
     * @return the position
     */
    long getPosition();


    /**
     * Retrieve the name of this stream.
     *
     * @return the stream name.
     */
    String getName();
}
