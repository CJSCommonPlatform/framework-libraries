package uk.gov.justice.services.core.enveloper.spi;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import org.junit.jupiter.api.Test;

public class EnveloperProviderTest {

    @Test
    public void shouldProvideEnveloperProvider() throws Exception {
        final EnveloperProvider provider = EnveloperProvider.provider();

        assertThat(provider, notNullValue());
        assertThat(provider, instanceOf(DummyEnveloperProvider.class));
    }
}