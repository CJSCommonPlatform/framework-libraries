package uk.gov.justice.services.messaging.spi;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import org.junit.Test;

public class EnvelopeProviderTest {

    @Test
    public void shouldProvideEnvelopeProvider() throws Exception {
        final EnvelopeProvider provider = EnvelopeProvider.provider();

        assertThat(provider, notNullValue());
        assertThat(provider, instanceOf(DummyEnvelopeProvider.class));
    }
}