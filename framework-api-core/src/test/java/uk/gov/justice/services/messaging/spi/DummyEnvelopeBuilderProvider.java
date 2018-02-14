package uk.gov.justice.services.messaging.spi;

import uk.gov.justice.services.messaging.Envelope;
import uk.gov.justice.services.messaging.JsonEnvelope;

import java.util.function.Function;

public class DummyEnvelopeBuilderProvider implements EnvelopeBuilderProvider {

    @Override
    public Function<Object, JsonEnvelope> withMetadataFrom(final JsonEnvelope envelope) {
        return null;
    }

    @Override
    public Function<Object, JsonEnvelope> withMetadataFrom(final JsonEnvelope envelope, final String name) {
        return null;
    }

    @Override
    public <T> Function<T, Envelope<T>> withMetadataFrom(final T envelope) {
        return null;
    }

    @Override
    public <T> Function<T, Envelope<T>> withMetadataFrom(final T envelope, final String name) {
        return null;
    }
    
}
