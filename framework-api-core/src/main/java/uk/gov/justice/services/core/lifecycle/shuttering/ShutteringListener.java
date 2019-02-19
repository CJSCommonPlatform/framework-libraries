package uk.gov.justice.services.core.lifecycle.shuttering;

import uk.gov.justice.services.core.lifecycle.shuttering.events.ObjectShutteredEvent;
import uk.gov.justice.services.core.lifecycle.shuttering.events.ObjectUnshutteredEvent;
import uk.gov.justice.services.core.lifecycle.shuttering.events.ShutteringCompleteEvent;
import uk.gov.justice.services.core.lifecycle.shuttering.events.ShutteringRequestedEvent;
import uk.gov.justice.services.core.lifecycle.shuttering.events.UnshutteringCompleteEvent;
import uk.gov.justice.services.core.lifecycle.shuttering.events.UnshutteringRequestedEvent;

/**
 * Listener that can be implemented if your object need to be informed about the shuttered state of
 * the application.
 *
 * For example, if you have an MDB that needs to be shuttered during a particular process, it can
 * implement the shutteringRequested(...) method and shutter itself. Once it has completed
 * shuttering, it can then fire shutteringComplete(...) to let other processes know that it
 * shuttered and now it is safe to proceed.
 */
public interface ShutteringListener {

    /**
     * Fired by any object that need to initiate shuttering. For example a JMX MBean
     *
     * @param shutteringRequestedEvent Containing data relevant the shuttering request
     */
    default void shutteringRequested(final ShutteringRequestedEvent shutteringRequestedEvent) {
    }

    /**
     * Fired by a Shutterable Object to inform any listeners that it's shuttering is complete. It
     * should include itself in the ObjectShutteringEvent that is sent to allow others to know which
     * Object has been shuttered
     */
    default void objectShuttered(final ObjectShutteredEvent objectShutteredEvent) {
    }

    /**
     * Fired by the shuttering process to inform any listeners that the shuttering process is now
     * complete
     */
    default void shutteringComplete(final ShutteringCompleteEvent shutteringCompleteEvent) {
    }

    default void unshutteringRequested(final UnshutteringRequestedEvent unshutteringRequestedEvent) {
    }

    default void objectUnshuttered(final ObjectUnshutteredEvent objectUnshutteredEvent) {
    }

    default void unshutteringComplete(final UnshutteringCompleteEvent unshutteringCompleteEvent) {
    }
}
