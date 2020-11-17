package uk.gov.justice.services.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;
import static uk.gov.justice.services.test.domain.AggregateWrapper.aggregateWrapper;

import uk.gov.justice.domain.aggregate.Aggregate;
import uk.gov.justice.services.test.domain.AggregateWrapper;

import org.junit.Test;

public class AggregateExceptionsTest {

    @Test
    public void testNoMethodsFound() throws Exception {

        final AggregateWrapper aggregateWrapper = aggregateWrapper().initialiseFromClass(DummyAggregate.class.getSimpleName());

        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () ->
                aggregateWrapper.invokeMethod("notExisting")
        );

        assertThat(illegalArgumentException.getMessage(), is("No method found"));
    }

    @Test
    public void testTooManyMethodsFound() throws Exception {

        final AggregateWrapper aggregateWrapper = aggregateWrapper().initialiseFromClass(DummyAggregate.class.getSimpleName());

        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () ->
                aggregateWrapper.invokeMethod("apply", "argsA")
        );

        assertThat(illegalArgumentException.getMessage(), is("Too many matching methods found"));
    }

    public static class DummyAggregate implements Aggregate {
        public void apply(final String id) {
        }

        @Override
        public Object apply(final Object id) {
            return Void.class;
        }
    }
}
