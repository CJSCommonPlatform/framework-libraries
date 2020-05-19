package uk.gov.justice.services.messaging.spi;

import uk.gov.justice.services.messaging.Envelope;
import uk.gov.justice.services.messaging.Metadata;
import uk.gov.justice.services.messaging.MetadataBuilder;

import javax.json.JsonObject;

public class DummyEnvelopeProvider implements EnvelopeProvider {

    @Override
    public <T> Envelope<T> envelopeFrom(final Metadata metadata, final T payload) {
        return null;
    }

    @Override
    public <T> Envelope<T> envelopeFrom(final MetadataBuilder metadataBuilder, final T payload) {
        return null;
    }

    @Override
    public MetadataBuilder metadataBuilder() {
        return null;
    }

    @Override
    public MetadataBuilder metadataFrom(final Metadata metadata) {
        return null;
    }

    @Override
    public MetadataBuilder metadataFrom(final JsonObject jsonObject) {
        return null;
    }
}
