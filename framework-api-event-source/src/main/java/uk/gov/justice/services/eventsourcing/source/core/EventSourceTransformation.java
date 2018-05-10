package uk.gov.justice.services.eventsourcing.source.core;

import uk.gov.justice.services.eventsourcing.source.core.exception.EventStreamException;

import java.util.UUID;

/**
 * Event Source that can be cloned.
 */
public interface EventSourceTransformation {

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
