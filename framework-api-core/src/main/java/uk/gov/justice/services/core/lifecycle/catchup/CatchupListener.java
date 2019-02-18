package uk.gov.justice.services.core.lifecycle.catchup;

import uk.gov.justice.services.core.lifecycle.catchup.events.CatchupCompletedEvent;
import uk.gov.justice.services.core.lifecycle.catchup.events.CatchupRequestedEvent;
import uk.gov.justice.services.core.lifecycle.catchup.events.CatchupStartedEvent;

public interface CatchupListener {

    default void catchupRequested(@SuppressWarnings("unused") final CatchupRequestedEvent catchupRequestedEvent) {}
    default void catchupStarted(@SuppressWarnings("unused") final CatchupStartedEvent catchupStartedEvent) {}
    default void catchupCompleted(@SuppressWarnings("unused") final CatchupCompletedEvent catchupCompletedEvent) {}
}
