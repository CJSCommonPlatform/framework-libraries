package uk.gov.justice.services.messaging;

import uk.gov.justice.services.messaging.spi.EnvelopeBuilderProvider;

import java.util.function.Function;

public interface EnvelopeBuilder {
    /**
     * Provides a function that wraps the provided object into a new {@link JsonEnvelope} using
     * metadata from the given envelope.
     *
     * @param envelope - the envelope containing source metadata.
     * @return a function that wraps objects into an envelope.
     */
    static Function<Object, JsonEnvelope> withMetadataFrom(final JsonEnvelope envelope)  {
        return EnvelopeBuilderProvider.provider().withMetadataFrom(envelope);
    }

    /**
     * Provides a function that wraps the provided object into a new {@link JsonEnvelope} using
     * metadata from the given envelope, except the name.
     *
     * @param envelope - the envelope containing source metadata.
     * @param name     - name of the payload.
     * @return a function that wraps objects into an envelope.
     */
    static Function<Object, JsonEnvelope> withMetadataFrom(final JsonEnvelope envelope, final String name) {
        return EnvelopeBuilderProvider.provider().withMetadataFrom(envelope, name);
    }

    static <T> Function<T, Envelope<T>> withMetadataFrom(final T envelope) {
        return EnvelopeBuilderProvider.provider().withMetadataFrom(envelope);
    }


    static <T> Function<T, Envelope<T>> withMetadataFrom(final T envelope, final String name) {
        return EnvelopeBuilderProvider.provider().withMetadataFrom(envelope);
    }

}
