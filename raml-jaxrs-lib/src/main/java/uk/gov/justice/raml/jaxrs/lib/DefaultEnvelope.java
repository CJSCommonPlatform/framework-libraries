package uk.gov.justice.raml.jaxrs.lib;

/**
 * Created by david on 15/02/16.
 */
public class DefaultEnvelope<M> implements Envelope<M> {

    private final M message;
    private final Metadata metadata;

    public DefaultEnvelope(final M message, final Metadata metadata) {
        this.message = message;
        this.metadata = metadata;
    }

    @Override
    public M getMessage() {
        return message;
    }

    @Override
    public Metadata getMetadata() {
        return metadata;
    }
}
