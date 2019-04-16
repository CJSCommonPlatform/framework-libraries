package uk.gov.justice.services.eventsourcing.source.core;

import uk.gov.justice.services.eventsourcing.source.core.exception.PublishedEventException;

/**
 * To manage Published event log
 */
public interface PublishedEventSourceTransformation {

    /**
     * Deletes all PublishedEvents in the published_event table
     */
    void deleteAllPublishedEvents() throws PublishedEventException;

    /**
     * Populates PublishedEvents into the published_event table
     */
    void populatePublishedEvents() throws PublishedEventException;

}
