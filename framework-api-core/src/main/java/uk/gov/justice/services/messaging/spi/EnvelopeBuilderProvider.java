package uk.gov.justice.services.messaging.spi;

import uk.gov.justice.services.messaging.Envelope;
import uk.gov.justice.services.messaging.JsonEnvelope;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.function.Function;

public interface EnvelopeBuilderProvider {

    static EnvelopeBuilderProvider provider() {
        final ServiceLoader<EnvelopeBuilderProvider> loader = ServiceLoader.load(EnvelopeBuilderProvider.class);
        final Iterator<EnvelopeBuilderProvider> iterator = loader.iterator();

        if (iterator.hasNext()) {
            return iterator.next();
        }

        throw new EnvelopeBuilderProviderNotFoundException("No EnvelopeBuilderProvider implementation found");
    }

    Function<Object, JsonEnvelope> withMetadataFrom(final JsonEnvelope envelope);
    Function<Object, JsonEnvelope> withMetadataFrom(final JsonEnvelope envelope, final String name);
    <T> Function<T, Envelope<T>> withMetadataFrom(final T envelope);
    <T> Function<T, Envelope<T>> withMetadataFrom(final T envelope, final String name);

}
