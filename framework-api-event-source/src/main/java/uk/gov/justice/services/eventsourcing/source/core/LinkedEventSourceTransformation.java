package uk.gov.justice.services.eventsourcing.source.core;

import uk.gov.justice.services.eventsourcing.source.core.exception.LinkedEventException;

/**
 * To manage linked event log
 */
public interface LinkedEventSourceTransformation {

    /**
     * Truncates the Linked event log
     */
    void truncate() throws LinkedEventException;

    /**
     * Populates linked event log
     */
    void populate() throws LinkedEventException;

}
