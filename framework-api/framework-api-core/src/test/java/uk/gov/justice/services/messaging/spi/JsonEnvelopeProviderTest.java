package uk.gov.justice.services.messaging.spi;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

import java.util.ServiceLoader;

public class JsonEnvelopeProviderTest {

    @Test
    public void shouldProvideJsonEnvelopeProvider() throws Exception {
        final JsonEnvelopeProvider provider = JsonEnvelopeProvider.provider();

        assertThat(provider, notNullValue());
        assertThat(provider, instanceOf(DummyJsonEnvelopeProvider.class));
    }

    @Test
    public void shouldCacheProvider() throws Exception {
        final EnvelopeProvider providerFirst = EnvelopeProvider.provider();
        final EnvelopeProvider providerSecond = EnvelopeProvider.provider();

        assertThat(providerFirst, CoreMatchers.is(providerSecond));
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
