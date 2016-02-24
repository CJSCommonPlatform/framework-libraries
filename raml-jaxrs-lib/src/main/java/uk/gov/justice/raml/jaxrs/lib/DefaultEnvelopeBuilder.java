package uk.gov.justice.raml.jaxrs.lib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonObject;
import javax.ws.rs.core.HttpHeaders;

/**
 * Created by david on 15/02/16.
 */
public class DefaultEnvelopeBuilder implements EnvelopeBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultEnvelopeBuilder.class);

    private Metadata metadata = null;
    private JsonObject body;

    @Override
    public Envelope<JsonObject> build() {
        return new DefaultEnvelope<>(body, metadata);
    }

    @Override
    public EnvelopeBuilder withMetadataFrom(HttpHeaders headers) {
        for (String key : headers.getRequestHeaders().keySet()) {
            for (String value : headers.getRequestHeader(key)) {
                LOGGER.info(key + " : " + value);
            }
        }
        return this;
    }

    @Override
    public EnvelopeBuilder withBody(JsonObject body) {
        this.body = body;
        return this;
    }

}
