package uk.gov.justice.services.messaging.spi;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Spliterators.emptySpliterator;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.fail;

import java.util.Spliterator;

import org.junit.Test;

public class JsonEnvelopeProviderSelectorTest {

    @Test
    public void shouldReturnJsonEnvelopeProvider() throws Exception {

        final JsonEnvelopeProvider dummyJsonEnvelopeProvider = new DummyJsonEnvelopeProvider();
        final Spliterator<JsonEnvelopeProvider> spliterator = singletonList(dummyJsonEnvelopeProvider).spliterator();

        final JsonEnvelopeProvider selectedJsonEnvelopeProvider = new JsonEnvelopeProviderSelector().selectFrom(spliterator);

        assertThat(selectedJsonEnvelopeProvider, notNullValue());
        assertThat(selectedJsonEnvelopeProvider, instanceOf(DummyJsonEnvelopeProvider.class));
    }

    @Test
    public void shouldReturnHighPriorityJsonEnvelopeProvider() throws Exception {

        final Spliterator<JsonEnvelopeProvider> spliterator = asList(
                new DummyJsonEnvelopeProvider(),
                new DummyJsonEnvelopeProvider(),
                new HighPriorityJsonEnvelopeProvider()).spliterator();

        final JsonEnvelopeProvider selectedJsonEnvelopeProvider = new JsonEnvelopeProviderSelector().selectFrom(spliterator);

        assertThat(selectedJsonEnvelopeProvider, notNullValue());
        assertThat(selectedJsonEnvelopeProvider, instanceOf(HighPriorityJsonEnvelopeProvider.class));
    }

    @Test
    public void shouldThrowJsonEnvelopeProviderNotFoundExceptionIfNoJsonEnvelopeProviderPresent() {

        try {
            new JsonEnvelopeProviderSelector().selectFrom(emptySpliterator());
            fail();
        } catch (final JsonEnvelopeProviderNotFoundException e) {
            assertThat(e.getMessage(), is("No JsonEnvelopeProvider implementation found"));
        }
    }
}
