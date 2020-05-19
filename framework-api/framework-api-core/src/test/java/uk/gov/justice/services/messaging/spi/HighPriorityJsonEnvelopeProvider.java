package uk.gov.justice.services.messaging.spi;

import static java.lang.Integer.MIN_VALUE;

import uk.gov.justice.services.messaging.JsonEnvelope;
import uk.gov.justice.services.messaging.Metadata;
import uk.gov.justice.services.messaging.MetadataBuilder;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

public class HighPriorityJsonEnvelopeProvider implements JsonEnvelopeProvider {

    @Override
    public JsonEnvelope envelopeFrom(final Metadata metadata, final JsonValue payload) {
        return null;
    }

    @Override
    public JsonEnvelope envelopeFrom(final MetadataBuilder metadataBuilder, final JsonValue payload) {
        return null;
    }

    @Override
    public JsonEnvelope envelopeFrom(final MetadataBuilder metadataBuilder, final JsonObjectBuilder payloadBuilder) {
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

    @Override
    public int priority() {
        return MIN_VALUE;
    }
}
