package uk.gov.justice.services.eventsourcing.source.core;

import uk.gov.justice.services.messaging.JsonEnvelope;

import java.util.UUID;
import java.util.stream.Stream;

/**
 * Source of event streams and Events.
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
}
