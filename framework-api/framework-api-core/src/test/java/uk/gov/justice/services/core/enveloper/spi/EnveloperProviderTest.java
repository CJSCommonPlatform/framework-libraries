package uk.gov.justice.services.core.enveloper.spi;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import org.junit.jupiter.api.Test;
import uk.gov.justice.services.messaging.spi.EnvelopeProvider;
import uk.gov.justice.services.messaging.spi.EnvelopeProviderSelector;

import java.util.ServiceLoader;

public class EnveloperProviderTest {

    @Test
    public void shouldProvideEnveloperProvider() throws Exception {
        final EnveloperProvider provider = EnveloperProvider.provider();

        assertThat(provider, notNullValue());
        assertThat(provider, instanceOf(DummyEnveloperProvider.class));
    }

    @Test
    public void shouldCacheProvider() throws Exception {
        final EnvelopeProvider providerFirst = EnvelopeProvider.provider();
        final EnvelopeProvider providerSecond = EnvelopeProvider.provider();

        assertThat(providerFirst, is(providerSecond));
    }

    @Test
    public void shouldNotCacheProvider() throws Exception {
        final EnvelopeProvider envelopeProviderFirst = new EnvelopeProviderSelector()
                .selectFrom(ServiceLoader.load(EnvelopeProvider.class).spliterator());
        final EnvelopeProvider envelopeProviderSecond = new EnvelopeProviderSelector()
                .selectFrom(ServiceLoader.load(EnvelopeProvider.class).spliterator());


        assertThat(envelopeProviderFirst, not(envelopeProviderSecond));
    }
}