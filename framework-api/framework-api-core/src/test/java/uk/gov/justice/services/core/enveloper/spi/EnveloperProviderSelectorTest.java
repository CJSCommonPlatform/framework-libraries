package uk.gov.justice.services.core.enveloper.spi;

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

public class EnveloperProviderSelectorTest {

    @Test
    public void shouldReturnEnveloperProvider() throws Exception {

        final EnveloperProvider dummyEnveloperProvider = new DummyEnveloperProvider();
        final Spliterator<EnveloperProvider> spliterator = singletonList(dummyEnveloperProvider).spliterator();

        final EnveloperProvider selectedEnveloperProvider = new EnveloperProviderSelector().selectFrom(spliterator);

        assertThat(selectedEnveloperProvider, notNullValue());
        assertThat(selectedEnveloperProvider, instanceOf(DummyEnveloperProvider.class));
    }

    @Test
    public void shouldReturnHighPriorityEnveloperProvider() throws Exception {

        final Spliterator<EnveloperProvider> spliterator = asList(
                new DummyEnveloperProvider(),
                new DummyEnveloperProvider(),
                new HighPriorityEnveloperProvider()).spliterator();

        final EnveloperProvider selectedEnveloperProvider = new EnveloperProviderSelector().selectFrom(spliterator);

        assertThat(selectedEnveloperProvider, notNullValue());
        assertThat(selectedEnveloperProvider, instanceOf(HighPriorityEnveloperProvider.class));
    }

    @Test
    public void shouldThrowEnveloperProviderNotFoundExceptionIfNoEnveloperProviderPresent() {

        try {
            new EnveloperProviderSelector().selectFrom(emptySpliterator());
            fail();
        } catch (final EnveloperProviderNotFoundException e) {
            assertThat(e.getMessage(), is("No EnveloperProvider implementation found"));
        }
    }
}