package uk.gov.justice.services.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static uk.gov.justice.services.test.domain.AggregateWrapper.aggregateWrapper;

import uk.gov.justice.services.test.domain.AggregateWrapper;
import uk.gov.justice.services.test.domain.aggregate.GenericAggregate;
import uk.gov.justice.services.test.domain.event.InitialEventA;
import uk.gov.justice.services.test.domain.event.InitialEventB;

import java.util.List;

import org.junit.jupiter.api.Test;

public class AggregateInitialisationTest extends AggregateTestAssertions {

    @Test
    public void shouldInitialiseAggregateWithNoInitialEvents() throws Exception {
        AggregateWrapper aggregateWrapper = aggregateWrapper().initialiseFromClass(GenericAggregate.class.getSimpleName());

        assertThat(aggregateWrapper.aggregate(), instanceOf(GenericAggregate.class));
        assertThat(((GenericAggregate) aggregateWrapper.aggregate()).appliedEvents(), empty());
    }

    @Test
    public void shouldNotInitialiseAggregateIfAlreadyInitialised() throws Exception {
        AggregateWrapper aggregateWrapper = aggregateWrapper().initialiseFromClass(GenericAggregate.class.getSimpleName());

        assertThat(aggregateWrapper.aggregate(), sameInstance(aggregateWrapper.initialiseFromClass(GenericAggregate.class.getSimpleName()).aggregate()));
    }

    @Test
    public void shouldCreateAggregateWithInitialEvents() throws Exception {
        AggregateWrapper aggregateWrapper = aggregateWrapper()
                .withInitialEventsFromFiles("event-a,event-b")
                .initialiseFromClass(GenericAggregate.class.getSimpleName());

        assertThat(aggregateWrapper.aggregate(), instanceOf(GenericAggregate.class));

        final List<Object> appliedEvents = ((GenericAggregate) aggregateWrapper.aggregate()).appliedEvents();

        assertThat(appliedEvents, hasSize(2));
        assertAppliedEvent(InitialEventA.class, appliedEvents.get(0), "5c5a1d30-0414-11e7-93ae-92361f002671", "Abc123");
        assertAppliedEvent(InitialEventB.class, appliedEvents.get(1), "5c5a1d30-0414-11e7-93ae-92361f002672", "BCD123");
    }

    @Test
    public void shouldThrowExceptionIfEventFileDoesNotExist() throws Exception {

        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () ->
                aggregateWrapper()
                        .withInitialEventsFromFiles("non-existent")
                        .initialiseFromClass(GenericAggregate.class.getSimpleName())
        );

        assertThat(illegalArgumentException.getMessage(), is("Error reading/parsing json file: non-existent"));
    }

    @Test
    public void shouldThrowExceptionIfEventClassDoesNotExist() throws Exception {

        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () ->
                aggregateWrapper()
                        .withInitialEventsFromFiles("event-no-class")
                        .initialiseFromClass(GenericAggregate.class.getSimpleName())
        );
        
        assertThat(illegalArgumentException.getMessage(), is("Error applying initial event. Event class not found"));
    }
}
