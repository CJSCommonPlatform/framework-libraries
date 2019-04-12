package uk.gov.justice.services.eventsourcing.source.core;

import uk.gov.justice.services.eventsourcing.source.core.exception.PublishedEventException;

/**
 * To manage Published event log
 */
public interface PublishedEventSourceTransformation {

    /**
     * Truncates the Published event log
     */
    void truncate() throws PublishedEventException;

    /**
     * Populates Published event log
     */
    void populate() throws PublishedEventException;

}
