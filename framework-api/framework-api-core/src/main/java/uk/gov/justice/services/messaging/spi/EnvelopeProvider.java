package uk.gov.justice.services.messaging.spi;

import static java.lang.Integer.MAX_VALUE;

import uk.gov.justice.services.messaging.Envelope;
import uk.gov.justice.services.messaging.Metadata;
import uk.gov.justice.services.messaging.MetadataBuilder;

import java.util.ServiceLoader;

import javax.json.JsonObject;
import javax.json.JsonValue;

/**
 * Interface for EnvelopeProvider implementations to provide methods for constructing
 * {@link Envelope} and {@link MetadataBuilder} instances.
 *
 * Call the static method {@code EnvelopeProvider.provider()} to retrieve the
 * EnvelopeProvider instance from service dependency on the classpath.
 */
public interface EnvelopeProvider {

    EnvelopeProvider ENVELOPE_PROVIDER = new EnvelopeProviderSelector()
            .selectFrom(ServiceLoader.load(EnvelopeProvider.class).spliterator());

    /**
     * Loads an implementation of EnvelopeProvider using the {@link ServiceLoader} mechanism. An
     * instance of the first implementing class from the loader list is returned.
     *
     * @return an instance of EnvelopeProvider
     * @throws EnvelopeProviderNotFoundException if no implementations of EnvelopeProvider
     *                                           are found
     */
    static EnvelopeProvider provider() {
        return ENVELOPE_PROVIDER;
    }

    /**
     * Provide an instance of a {@link Envelope} with given {@link Metadata} and {@link
     * JsonValue}.
     *
     * @param metadata the Metadata to be added to the Envelope
     * @param payload  the JsonValue to be added to the Envelope
     * @return the Envelope instance
     */
    <T> Envelope<T> envelopeFrom(final Metadata metadata, final T payload);

    /**
     * Provide an instance of a {@link Envelope} with given {@link MetadataBuilder} and {@link
     * JsonValue}
     *
     * @param metadataBuilder the MetadataBuilder to be used to build {@link Metadata}
     * @param payload         the JsonValue to be added to the Envelope
     * @return the Envelope instance
     */
    <T> Envelope<T> envelopeFrom(final MetadataBuilder metadataBuilder, final T payload);

    /**
     * Provide an instance of a {@link MetadataBuilder}
     *
     * @return the MetadataBuilder instance
     */
    MetadataBuilder metadataBuilder();

    /**
     * Provide an instance of a {@link MetadataBuilder} from the given {@link Metadata}
     *
     * @param metadata the Metadata to add to the MetadataBuilder
     * @return the MetadataBuilder instance
     */
    MetadataBuilder metadataFrom(final Metadata metadata);

    /**
     * Provide an instance of a {@link MetadataBuilder} from the given {@link JsonObject}
     *
     * @param jsonObject the JsonObject to add to the MetadataBuilder
     * @return the MetadataBuilder instance
     */
    MetadataBuilder metadataFrom(final JsonObject jsonObject);

    /**
     * Return the priority level for the envelope provider implementation.
     * 0                 - Highest priority
     * Integer.MAX_VALUE - Lowest priority - Default
     *
     * @return MAX_VALUE - Default lowest priority
     */
    default int priority() {
        return MAX_VALUE;
    }
}
