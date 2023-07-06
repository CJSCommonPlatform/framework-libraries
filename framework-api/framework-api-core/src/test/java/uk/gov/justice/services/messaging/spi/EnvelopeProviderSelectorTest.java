package uk.gov.justice.services.messaging.spi;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Spliterators.emptySpliterator;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Spliterator;

import org.junit.jupiter.api.Test;

public class EnvelopeProviderSelectorTest {

    @Test
    public void shouldReturnEnvelopeProvider() throws Exception {

        final EnvelopeProvider envelopeProvider = new DummyEnvelopeProvider();
        final Spliterator<EnvelopeProvider> spliterator = singletonList(envelopeProvider).spliterator();

        final EnvelopeProvider selectedEnvelopeProvider = new EnvelopeProviderSelector().selectFrom(spliterator);

        assertThat(selectedEnvelopeProvider, notNullValue());
        assertThat(selectedEnvelopeProvider, instanceOf(DummyEnvelopeProvider.class));
    }

    @Test
    public void shouldReturnHighPriorityEnvelopeProvider() throws Exception {

        final Spliterator<EnvelopeProvider> spliterator = asList(
                new DummyEnvelopeProvider(),
                new DummyEnvelopeProvider(),
                new HighPriorityEnvelopeProvider()).spliterator();

        final EnvelopeProvider selectedEnvelopeProvider = new EnvelopeProviderSelector().selectFrom(spliterator);

        assertThat(selectedEnvelopeProvider, notNullValue());
        assertThat(selectedEnvelopeProvider, instanceOf(HighPriorityEnvelopeProvider.class));
    }

    @Test
    public void shouldThrowEnvelopeProviderNotFoundExceptionIfNoEnvelopeProviderPresent() {

        try {
            new EnvelopeProviderSelector().selectFrom(emptySpliterator());
            fail();
        } catch (final EnvelopeProviderNotFoundException e) {
            assertThat(e.getMessage(), is("No EnvelopeProvider implementation found"));
        }
    }
}
