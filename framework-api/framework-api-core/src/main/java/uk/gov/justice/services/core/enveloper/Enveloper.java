package uk.gov.justice.services.core.enveloper;

import uk.gov.justice.services.core.enveloper.spi.EnveloperProvider;
import uk.gov.justice.services.messaging.Envelope;
import uk.gov.justice.services.messaging.JsonEnvelope;
import uk.gov.justice.services.messaging.Metadata;

import java.util.function.Function;

public interface Enveloper {

    /**
     * Provides a function that wraps the provided object into a new {@link JsonEnvelope} using
     * metadata from the given envelope.
     *
     * @deprecated use Enveloper.envelop(T payload)
     *                          .withName(String name)
     *                          .withMetadataFrom(Envelope&lt;?&gt; envelope) instead
     * @param envelope - the envelope containing source metadata.
     * @return a function that wraps objects into an envelope.
     */
    @Deprecated
    Function<Object, JsonEnvelope> withMetadataFrom(final JsonEnvelope envelope);

    /**
     * Provides a function that wraps the provided object into a new {@link JsonEnvelope} using
     * metadata from the given envelope, except the name.
     *
     * @deprecated use Enveloper.envelop(T payload)
     *                          .withName(String name)
     *                          .withMetadataFrom(Envelope&lt;?&gt; envelope) instead
     * @param envelope - the envelope containing source metadata.
     * @param name     - name of the payload.
     * @return a function that wraps objects into an envelope.
     */
    @Deprecated
    Function<Object, JsonEnvelope> withMetadataFrom(final JsonEnvelope envelope, final String name);

    /**
     * Provides a function that wraps the provided object into a new {@link JsonEnvelope} using
     * metadata from the given Envelope, except the name.
     *
     * @param envelope The original {@link JsonEnvelope}
     * @return Function&lt;Object, JsonEnvelope&gt;
     */
    static Function<Object, JsonEnvelope> toEnvelopeWithMetadataFrom(final Envelope<?> envelope) {
        return EnveloperProvider.provider().toEnvelopeWithMetadataFrom(envelope);
    }

    /**
     * Creates an {@link Envelope} through {@link EnveloperBuilder}
     * @param payload The payload for the envelope
     * @param <T> The envelope type
     * @return EnveloperBuilder
     */
    static <T> EnveloperBuilder<T> envelop(final T payload) {
        return EnveloperProvider.provider().envelop(payload);
    }

    /**
     * A helper interface to provide an instance of a {@link Envelope} with given {@link Metadata}
     */
    interface EnveloperBuilder<T> {

        /**
         * Helper interface method to set name
         *
         * @param name the name to be added to the Envelope.
         * @return the EnveloperBuilder instance
         */
        EnveloperBuilder<T> withName(String name);

        /**
         * Helper interface method to apply metadata
         *
         * @param envelope the Envelope with metadata to be applied on new envelope.
         * @return the EnveloperBuilder instance
         */
        Envelope<T> withMetadataFrom(final Envelope<?> envelope);

    }
}
