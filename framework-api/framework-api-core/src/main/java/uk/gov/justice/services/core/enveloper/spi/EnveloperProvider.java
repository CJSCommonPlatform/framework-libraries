package uk.gov.justice.services.core.enveloper.spi;

import static java.lang.Integer.MAX_VALUE;

import uk.gov.justice.services.core.enveloper.Enveloper;
import uk.gov.justice.services.messaging.Envelope;
import uk.gov.justice.services.messaging.JsonEnvelope;

import java.util.ServiceLoader;
import java.util.function.Function;

/**
 * Interface for EnveloperProvider implementations to provide methods for constructing
 * {@link Envelope} instances.
 *
 * Call the static method {@code EnveloperProvider.provider()} to retrieve the
 * EnveloperProvider instance from service dependency on the classpath.
 */
public interface EnveloperProvider {

    EnveloperProvider ENVELOPER_PROVIDER = new EnveloperProviderSelector()
            .selectFrom(ServiceLoader.load(EnveloperProvider.class).spliterator());

    /**
     * Loads an implementation of EnveloperProvider using the {@link ServiceLoader} mechanism. An
     * instance of the first implementing class from the loader list is returned.
     *
     * @return an instance of EnveloperProvider
     * @throws EnveloperProviderNotFoundException if no implementations of EnveloperProvider
     *                                           are found
     */
    static EnveloperProvider provider() {
        return ENVELOPER_PROVIDER;
    }

    /**
     * Provide an instance of a {@link Envelope} through EnveloperBuilder with given payload
     *
     * @param payload  the payload Object to be added to the Envelope
     * @return the EnveloperBuilder instance
     */
    <T> Enveloper.EnveloperBuilder<T> envelop(final T payload);

    /**
     * Provides a function that wraps the provided object into a new {@link JsonEnvelope} using
     * metadata from the given Envelope, except the name.
     *
     * @param envelope
     * @return Function<Object, JsonEnvelope>
     */
    Function<Object, JsonEnvelope> toEnvelopeWithMetadataFrom(final Envelope<?> envelope);

    /**
     * Return the priority level for the enveloper provider implementation.
     * 0                 - Highest priority
     * Integer.MAX_VALUE - Lowest priority - Default
     *
     * @return MAX_VALUE - Default lowest priority
     */
    default int priority() {
        return MAX_VALUE;
    }
}
