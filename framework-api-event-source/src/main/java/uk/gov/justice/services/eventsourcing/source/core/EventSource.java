package uk.gov.justice.services.eventsourcing.source.core;

import uk.gov.justice.services.eventsourcing.source.core.exception.EventStreamException;

import java.util.UUID;
import java.util.stream.Stream;

/**
 * Source of event streams.
 */
public interface EventSource {

    /**
     * Get an event stream by stream id.
     *
     * @param streamId the id of the stream to be retrieved
     * @return the {@link EventStream}
     */
    EventStream getStreamById(final UUID streamId);

    /**
     * Get a stream of event streams.
     *
     * @return a stream of {@link EventStream}s
     */
    Stream<EventStream> getStreams();

    /**
     * Get a stream of event streams.
     *
     * @param position the position in the stream of streams to start from
     * @return a stream of {@link EventStream}s
     */
    Stream<EventStream> getStreamsFrom(final long position);

    /**
     * Clones the stream into a new stream id.
     *
     * @param streamId the id of the stream to be cloned
     * @return the id of the clone
     * @throws EventStreamException if the cloning of the stream fails
     */
    UUID cloneStream(final UUID streamId) throws EventStreamException;

    /**
     * Clears all of the events from a stream id.
     *
     * @param streamId the id of the stream to be cleared
     */
    void clearStream(final UUID streamId) throws EventStreamException;
}
