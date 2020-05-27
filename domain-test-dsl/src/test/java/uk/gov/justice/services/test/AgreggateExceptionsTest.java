package uk.gov.justice.services.test;

import uk.gov.justice.domain.aggregate.Aggregate;
import uk.gov.justice.services.test.domain.AggregateWrapper;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AgreggateExceptionsTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testNoMethodsFound() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("No method found");

        final AggregateWrapper aggregateWrapper = AggregateWrapper.aggregateWrapper().initialiseFromClass(DummyAggregate.class.getSimpleName());
        aggregateWrapper.invokeMethod("notExisting");
    }

    @Test
    public void testTooManyMethodsFound() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Too many matching methods found");

        final AggregateWrapper aggregateWrapper = AggregateWrapper.aggregateWrapper().initialiseFromClass(DummyAggregate.class.getSimpleName());

        aggregateWrapper.invokeMethod("apply", "argsA");
    }

    public static class DummyAggregate implements Aggregate {
        public void apply(final String id) { }
        @Override
        public Object apply(final Object id) {
            return Void.class;
        }
    }
}
