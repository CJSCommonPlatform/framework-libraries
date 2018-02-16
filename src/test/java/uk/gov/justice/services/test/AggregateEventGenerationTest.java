package uk.gov.justice.services.test;

import org.junit.Before;
import org.junit.Test;
import uk.gov.justice.services.test.domain.AggregateWrapper;
import uk.gov.justice.services.test.domain.aggregate.GenericAggregate;
import uk.gov.justice.services.test.domain.event.SthDoneWithIntArgEvent;
import uk.gov.justice.services.test.domain.event.SthDoneWithNoArgsEvent;
import uk.gov.justice.services.test.domain.event.SthDoneWithStringArgEvent;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static uk.gov.justice.services.test.DomainTest.generatedEventAsJsonNode;
import static uk.gov.justice.services.test.domain.AggregateWrapper.aggregateWrapper;

public class AggregateEventGenerationTest extends AggregateTestAssertions {

    private AggregateWrapper aggregateWrapper;

    @Before
    public void setup() throws InstantiationException, IllegalAccessException {
        aggregateWrapper = aggregateWrapper()
                .initialiseFromClass(GenericAggregate.class.getSimpleName());
    }

    @Test
    public void shouldReturnInfoAboutGeneratedEvent() throws Exception {
        aggregateWrapper.invokeMethod("doSthWithStringArg", "string-arg-file-abc");

        assertThat(aggregateWrapper.generatedEvents(), hasSize(1));
        assertThat(generatedEventAsJsonNode(aggregateWrapper.generatedEvents().get(0)).get("strArg").asText(), is("stringFormFileABC"));
        assertGeneratedEvent(aggregateWrapper.generatedEvents().get(0), SthDoneWithStringArgEvent.class, "context.sth-done-with-string-arg");
    }

    @Test
    public void shouldReturnInfoAboutGeneratedEventWithNoArgsInvocation() throws Exception {
        aggregateWrapper.invokeMethod("doSthWithNoArgs");

        assertThat(aggregateWrapper.generatedEvents(), hasSize(1));
        assertGeneratedEvent(aggregateWrapper.generatedEvents().get(0), SthDoneWithNoArgsEvent.class, "context.sth-done-no-args");
    }

    @Test
    public void shouldReturnInfoAboutTwoGeneratedEvents() throws Exception {
        aggregateWrapper.invokeMethod("doSthWithIntArg", "int-arg-file-123");
        aggregateWrapper.invokeMethod("doSthWithStringArg", "string-arg-file-abc");

        assertThat(aggregateWrapper.generatedEvents(), hasSize(2));
        assertGeneratedEvent(aggregateWrapper.generatedEvents().get(0), SthDoneWithIntArgEvent.class, "context.sth-done-with-int-arg");
        assertGeneratedEvent(aggregateWrapper.generatedEvents().get(1), SthDoneWithStringArgEvent.class, "context.sth-done-with-string-arg");
    }
}