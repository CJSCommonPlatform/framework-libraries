package uk.gov.justice.services.core.enveloper.spi;

import static java.lang.Integer.MIN_VALUE;

import uk.gov.justice.services.core.enveloper.Enveloper;
import uk.gov.justice.services.messaging.Envelope;
import uk.gov.justice.services.messaging.JsonEnvelope;

import java.util.function.Function;

public class HighPriorityEnveloperProvider implements EnveloperProvider {

    @Override
    public <T> Enveloper.EnveloperBuilder<T> envelop(final T payload) {
        return null;
    }

    @Override
    public Function<Object, JsonEnvelope> toEnvelopeWithMetadataFrom(final Envelope<?> envelope) {
        return null;
    }

    @Override
    public int priority() {
        return MIN_VALUE;
    }
}
