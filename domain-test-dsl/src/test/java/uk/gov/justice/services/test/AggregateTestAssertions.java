package uk.gov.justice.services.test;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.justice.services.test.DomainTest.eventNameFrom;

import uk.gov.justice.services.test.domain.AggregateWrapper;
import uk.gov.justice.services.test.domain.aggregate.GenericAggregate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.tuple.Pair;
import org.hamcrest.Matcher;

class AggregateTestAssertions {

    void assertAppliedEvent(Class<?> eventClass, Object appliedEvent, String id, String stringField) {
        assertThat(appliedEvent, instanceOf(eventClass));
        assertThat(appliedEvent, hasProperty("id", is(UUID.fromString(id))));
        assertThat(appliedEvent, hasProperty("stringField", is(stringField)));
    }

    void assertGeneratedEvent(Object generatedEvent, Class<?> eventClass, String eventName) {
        assertThat(generatedEvent, instanceOf(eventClass));
        assertThat(eventNameFrom(generatedEvent), is(eventName));
    }

    void assertMethodInvocations(AggregateWrapper aggregateWrapper, String methodName, int invocationCount, Matcher argsMatcher) {
        assertThat(methodInvocations(aggregateWrapper).size(), is(invocationCount));
        assertTrue(methodInvocations(aggregateWrapper).contains(methodName));
        assertThat(methodInvocationArgumentsOf(aggregateWrapper, 0), argsMatcher);
    }


    void assertMethodInvocations(final AggregateWrapper aggregateWrapper, final int invocationCount, String... methodNames) {
        assertTrue(methodInvocations(aggregateWrapper).size() == invocationCount);
        assertTrue(methodInvocations(aggregateWrapper).containsAll(Arrays.asList(methodNames)));
    }

    private List<String> methodInvocations(AggregateWrapper aggregateWrapper) {
        return ((GenericAggregate) aggregateWrapper.aggregate()).methodInvocations()
                .stream()
                .map(Pair::getLeft)
                .collect(toList());
    }

    Object[] methodInvocationArgumentsOf(final AggregateWrapper aggregateWrapper, final int invocationNumber) {
        return ((GenericAggregate) aggregateWrapper.aggregate()).methodInvocations().get(invocationNumber).getRight();
    }
}
