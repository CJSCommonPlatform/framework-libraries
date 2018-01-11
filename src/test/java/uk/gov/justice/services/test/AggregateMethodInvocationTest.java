package uk.gov.justice.services.test;

import org.junit.Before;
import org.junit.Test;
import uk.gov.justice.services.common.converter.ZonedDateTimes;
import uk.gov.justice.services.test.domain.AggregateWrapper;
import uk.gov.justice.services.test.domain.aggregate.GenericAggregate;
import uk.gov.justice.services.test.domain.arg.ComplexArgument;

import java.time.ZoneId;
import java.util.UUID;

import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.hamcrest.collection.IsArrayContainingInOrder.arrayContaining;
import static org.junit.Assert.assertThat;
import static uk.gov.justice.services.test.domain.AggregateWrapper.aggregateWrapper;

public class AggregateMethodInvocationTest extends AggregateTestAssertions {

    private AggregateWrapper aggregateWrapper;

    @Before
    public void setup() throws InstantiationException, IllegalAccessException {
        aggregateWrapper = aggregateWrapper()
                .initialiseFromClass(GenericAggregate.class.getSimpleName());
    }

    @Test
    public void shouldCallNoArgMethod() throws Exception {
        aggregateWrapper.invokeMethod("doSthWithNoArgs");

        assertMethodInvocations(aggregateWrapper, 1, "doSthWithNoArgs");
    }

    @Test
    public void shouldCallMethodWithStringArgumentFromFile() throws Exception {
        aggregateWrapper.invokeMethod("doSthWithStringArg", "string-arg-file-abc");

        assertMethodInvocations(aggregateWrapper, "doSthWithStringArg", 1,
                arrayContaining("stringFormFileABC"));
    }

    @Test
    public void shouldCallTwoMethods() throws Exception {
        aggregateWrapper.invokeMethod("doSthWithNoArgs");
        aggregateWrapper.invokeMethod("doSthWithStringArg", "string-arg-file-abc");

        assertMethodInvocations(aggregateWrapper, 2, "doSthWithNoArgs", "doSthWithStringArg");
    }

    @Test
    public void shouldCallMethodWithStringAndUUIDArgumentsFromFile() throws Exception {
        aggregateWrapper.invokeMethod("doSthWithStringAndUUIDArg", "string-uuid-args-file-bcd");

        assertMethodInvocations(aggregateWrapper, "doSthWithStringAndUUIDArg", 1,
                arrayContaining("stringFormFileBCD", UUID.fromString("6c5a1d30-0414-11e7-93ae-92361f002671")));
    }


    @Test
    public void shouldCallMethodWithBooleanArgFromFile() throws Exception {
        aggregateWrapper.invokeMethod("doSthWithBooleanArg", "boolean-arg-file");

        assertMethodInvocations(aggregateWrapper, "doSthWithBooleanArg", 1, arrayContaining(true));
    }

    @Test
    public void shouldCallMethodWithIntegerArgFromFile() throws Exception {
        aggregateWrapper.invokeMethod("doSthWithIntArg", "int-arg-file-123");

        assertMethodInvocations(aggregateWrapper, "doSthWithIntArg", 1, arrayContaining(123));
    }

    @Test
    public void shouldCallMethodWithLongArgFromFile() throws Exception {
        aggregateWrapper.invokeMethod("doSthWithLongArg", "long-arg-file-123");

        assertMethodInvocations(aggregateWrapper, "doSthWithLongArg", 1, arrayContaining(123L));
    }

    @Test
    public void shouldCallMethodWithDateTimeArgFromFile() throws Exception {
        aggregateWrapper.invokeMethod("doSthWithDateTimeArg", "date-time-arg-file");

        assertMethodInvocations(aggregateWrapper, "doSthWithDateTimeArg", 1,
                arrayContaining(ZonedDateTimes.fromString("2017-01-21T16:42:03.522Z")
                        .withZoneSameInstant(ZoneId.of("UTC"))));
    }

    @Test
    public void shouldCallMethodWithComplexArgFromFile() throws Exception {
        aggregateWrapper.invokeMethod("doSthWithComplexArg", "complex-arg-file");

        assertMethodInvocations(aggregateWrapper, 1, "doSthWithComplexArg");
        assertThat(methodInvocationArgumentsOf(aggregateWrapper, 0)[0],
                samePropertyValuesAs(new ComplexArgument("someString", 456, true)));
    }
}