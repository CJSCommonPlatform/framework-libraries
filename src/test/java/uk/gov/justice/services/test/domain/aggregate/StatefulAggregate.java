package uk.gov.justice.services.test.domain.aggregate;


import static uk.gov.justice.domain.aggregate.matcher.EventSwitcher.match;
import static uk.gov.justice.domain.aggregate.matcher.EventSwitcher.when;

import uk.gov.justice.domain.aggregate.Aggregate;
import uk.gov.justice.services.test.domain.event.CakeOrdered;
import uk.gov.justice.services.test.domain.event.SomethingElseHappened;
import uk.gov.justice.services.test.domain.event.SomethingHappened;

import java.util.UUID;
import java.util.stream.Stream;

public class StatefulAggregate implements Aggregate {

    private boolean eventApplied;

    public Stream<Object> doSomething(final UUID id) {
        if (eventApplied) {
            return apply(Stream.of(new SomethingElseHappened(id)));
        }
        return apply(Stream.of(new SomethingHappened(id)));
    }

    public Stream<Object> doSomethingTwice(final UUID id) {
        return apply(Stream.of(new SomethingHappened(id), new SomethingHappened(id)));
    }

    public Stream<Object> doNothing() {
        return Stream.empty();
    }

    @Override
    public Object apply(final Object event) {
        return match(event).with(
                when(SomethingHappened.class)
                        .apply(somethingHappenedEvent -> this.eventApplied = true),
                when(CakeOrdered.class)
                        .apply(cakeOrderedEvent -> doNothing()),
                when(SomethingElseHappened.class)
                        .apply(somethingElseHappenedEvent -> doNothing())
        );
    }
}
