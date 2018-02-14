package uk.gov.justice.services.messaging.spi;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import org.junit.Test;


public class EnvelopeBuilderProviderTest {

    @Test
    public void shouldProvideEnvelopeBuilderProvider() {
        final EnvelopeBuilderProvider envelopeBuilderProvider = EnvelopeBuilderProvider.provider();

        assertThat(envelopeBuilderProvider, notNullValue());
        assertThat(envelopeBuilderProvider, instanceOf(DummyEnvelopeBuilderProvider.class));

    }

}
